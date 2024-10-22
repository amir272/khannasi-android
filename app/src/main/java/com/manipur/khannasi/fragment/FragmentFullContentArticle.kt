package com.manipur.khannasi.fragment

import android.os.Bundle
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manipur.khannasi.R
import com.manipur.khannasi.adapter.ArticleCommentAdapter
import com.manipur.khannasi.constants.DISLIKE
import com.manipur.khannasi.constants.LIKE
import com.manipur.khannasi.constants.NONE
import com.manipur.khannasi.dto.ArticleComment
import com.manipur.khannasi.dto.UserBasics
import com.manipur.khannasi.dto.UserDetails
import com.manipur.khannasi.interfaces.EditorVisibilityCallback
import com.manipur.khannasi.interfaces.PostArticleWithEditor
import com.manipur.khannasi.repository.ArticleCommentRepository
import com.manipur.khannasi.roomdb.DatabaseProvider
import com.manipur.khannasi.roomrepo.ArticleVoteRepo
import com.manipur.khannasi.service.PostArticleVoteService
import com.manipur.khannasi.util.BackFunction.Companion.onBackButtonClicked
import com.manipur.khannasi.util.LoadingSpinner
import com.manipur.khannasi.util.SharedPreferencesRetriever
import com.manipur.khannasi.util.SharedViewModel
import com.manipur.khannasi.util.SpannedHtmlString.Companion.fromHtml
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class FragmentFullContentArticle : Fragment(), EditorVisibilityCallback, PostArticleWithEditor {

    private lateinit var articleVoteRepo: ArticleVoteRepo

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var richEditorFragment: RichEditorFragment
    private lateinit var addCommentButton: Button
    private lateinit var commentButtons: LinearLayout

    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentsAdapter: ArticleCommentAdapter

    private var currentArticleCommentsList: List<ArticleComment> = emptyList()
    var articleId: Long = 0
    var replyToCommentId = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_full_page, container, false)
        articleVoteRepo = ArticleVoteRepo(
            DatabaseProvider.getDatabase(requireContext()).articleVoteDao()
        )
        val userDetails: UserDetails? =
            SharedPreferencesRetriever.getDetails<UserDetails>(requireContext(), "UserDetails")
        val userBasics: UserBasics = userDetails?.userBasics ?: UserBasics()

        LoadingSpinner.hideLoadingSpinner(this, requireContext())
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        richEditorFragment = RichEditorFragment(50)
        richEditorFragment.editorVisibilityCallback = this
        richEditorFragment.postArticleWithEditor = this

        val tvOutput = view.findViewById<TextView>(R.id.full_content_text)
        Log.d("FragmentFullContent", "Content: ${arguments?.getString("content")}")
        val content = arguments?.getString("content")

        Log.d("FragmentFullContent", "ArticleId: ${arguments?.getLong("articleId", 0)}")
        val articleIdFromItem = arguments?.getLong("articleId", 0) ?: 0
        articleId = articleIdFromItem
        val htmlContent = content ?: "An error happened. Please try again later."
        val spanned: Spanned = fromHtml(htmlContent, requireContext(), tvOutput, 600, 900)
        tvOutput.text = spanned

        commentsRecyclerView = view.findViewById(R.id.comments_recycler_view)
        commentsRecyclerView.layoutManager = LinearLayoutManager(context)

        val articleCommentRepository = ArticleCommentRepository()
        articleCommentRepository.getCommentsByArticleId(articleIdFromItem) { comments ->
            if (comments != null) {
                sharedViewModel.retrievedArticleComments.value = comments
                currentArticleCommentsList = comments
                Log.d("ArticleComments", currentArticleCommentsList.toString())
                commentsAdapter = ArticleCommentAdapter(requireContext(), userBasics, comments, onReplyClick = { comment ->
                    showEditor(comment.commentId)
                }, onLikeClick = { articleIdToUpdate, commentId, liked ->
                    Log.d("ArticleFeedFragment", "Liking article $commentId")
                    run {
                        if (liked) {
                            PostArticleVoteService.postArticleVote(
                                articleIdToUpdate, commentId, LIKE, userBasics, articleVoteRepo, this
                            )
                        } else {
                            PostArticleVoteService.postArticleVote(
                                articleIdToUpdate, commentId, NONE, userBasics, articleVoteRepo, this
                            )
                        }
                    }
                    true
                }, onDislikeClick = { articleIdToUpdate, commentId, disliked ->
                    run {
                        if (disliked) {
                            Log.d("ArticleFeedFragment", "Disliking article")
                            PostArticleVoteService.postArticleVote(
                                articleIdToUpdate, commentId, DISLIKE, userBasics, articleVoteRepo, this
                            )
                        } else {
                            PostArticleVoteService.postArticleVote(
                                articleIdToUpdate, commentId, NONE, userBasics, articleVoteRepo, this
                            )
                        }
                    }
                    true
                }, CoroutineScope(Dispatchers.Main)
                )
                commentsRecyclerView.adapter = commentsAdapter

            }
            Log.d("ArticleFeedFragment", "Comments: $comments")
        }
        addCommentButton = view.findViewById(R.id.add_comment_button)
        commentButtons = view.findViewById(R.id.comment_buttons)
        addCommentButton.setOnClickListener {
            showEditor()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoadingSpinner.hideLoadingSpinner(this, requireContext())

        val toolbar = view.findViewById<Toolbar>(R.id.full_content_toolbar)
        toolbar.setNavigationIcon(R.drawable.back_button_vector)

        toolbar.setNavigationOnClickListener {
            LoadingSpinner.showLoadingSpinner(this, requireContext())
            onBackButtonClicked(this)
        }
    }

    override fun showEditor(replyTo: Long?) {
        if (replyTo != null) {
            replyToCommentId = replyTo
        }
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.editor_container, richEditorFragment)
        transaction.commit()

        view?.findViewById<LinearLayout>(R.id.editor_container)?.visibility = View.VISIBLE
        addCommentButton.visibility = View.GONE
    }

    override fun hideEditor() {
        val editorContainer = view?.findViewById<LinearLayout>(R.id.editor_container)
        editorContainer?.visibility = View.GONE
        addCommentButton.visibility = View.VISIBLE
    }

    override fun postArticleComment(articleComment: ArticleComment?) {

        val userDetails: UserDetails? =
            SharedPreferencesRetriever.getDetails<UserDetails>(requireContext(), "UserDetails")
        val userBasics: UserBasics = userDetails?.userBasics ?: UserBasics()
        val articleCommentToPost = ArticleComment(
            articleId = articleId,
            content = richEditorFragment.getContent(),
            userBasics = userBasics,
            replyTo = replyToCommentId,
            level = if (replyToCommentId == 0L) 0 else getDepthLevel(replyToCommentId) + 1
        )

        val articleCommentRepository = ArticleCommentRepository()
        articleCommentRepository.postArticleComment(articleCommentToPost) { postedComment ->
            Toast.makeText(requireContext(), postedComment?.commentId.toString(), Toast.LENGTH_SHORT).show()
            hideEditor()

            if (postedComment != null) {
                val updatedComments = currentArticleCommentsList.toMutableList()
                updatedComments.add(postedComment)
                currentArticleCommentsList = updatedComments

                // Refresh the adapter with the new list of comments
                commentsAdapter =
                    ArticleCommentAdapter(requireContext(), userBasics, currentArticleCommentsList, onReplyClick = { comment ->
                        showEditor(comment.commentId)
                    }, onLikeClick = { articleIdToUpdate, commentId, liked ->
                        run {
                            if (liked) {
                                Log.d("ArticleFeedFragment", "Liking article")
                                PostArticleVoteService.postArticleVote(
                                    articleIdToUpdate, commentId, LIKE, userBasics, articleVoteRepo, this
                                )
                            } else {
                                PostArticleVoteService.postArticleVote(
                                    articleIdToUpdate, commentId, NONE, userBasics, articleVoteRepo, this
                                )
                            }
                        }
                        true
                    }, onDislikeClick = { articleIdToUpdate, commentId, disliked ->
                        run {
                            if (disliked) {
                                Log.d("ArticleFeedFragment", "Disliking article")
                                PostArticleVoteService.postArticleVote(
                                    articleIdToUpdate, commentId, DISLIKE, userBasics, articleVoteRepo, this
                                )
                            } else {
                                PostArticleVoteService.postArticleVote(
                                    articleIdToUpdate, commentId, NONE, userBasics, articleVoteRepo, this
                                )
                            }
                        }
                        true
                    }, CoroutineScope(Dispatchers.Main)
                    )
                commentsRecyclerView.adapter = commentsAdapter
            }
        }
    }

    private fun getDepthLevel(replyTo: Long): Int {
        val comment = currentArticleCommentsList.find { it.commentId == replyTo }
        return comment?.level ?: 0
    }
}