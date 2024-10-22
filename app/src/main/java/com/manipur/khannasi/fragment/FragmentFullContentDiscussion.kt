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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manipur.khannasi.R
import com.manipur.khannasi.adapter.DiscussionCommentAdapter
import com.manipur.khannasi.constants.DISLIKE
import com.manipur.khannasi.constants.LIKE
import com.manipur.khannasi.constants.NONE
import com.manipur.khannasi.dto.DiscussionComment
import com.manipur.khannasi.dto.UserBasics
import com.manipur.khannasi.dto.UserDetails
import com.manipur.khannasi.interfaces.EditorVisibilityCallback
import com.manipur.khannasi.interfaces.PostDiscussionWithEditor
import com.manipur.khannasi.repository.ArticleCommentRepository
import com.manipur.khannasi.repository.DiscussionCommentRepository
import com.manipur.khannasi.roomdb.DatabaseProvider
import com.manipur.khannasi.roomrepo.DiscussionVoteRepo
import com.manipur.khannasi.service.PostDiscussionVoteService
import com.manipur.khannasi.util.BackFunction.Companion.onBackButtonClicked
import com.manipur.khannasi.util.LoadingSpinner
import com.manipur.khannasi.util.RetryHelper
import com.manipur.khannasi.util.SharedPreferencesRetriever
import com.manipur.khannasi.util.SharedViewModel
import com.manipur.khannasi.util.SpannedHtmlString.Companion.fromHtml
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class FragmentFullContentDiscussion : Fragment(), EditorVisibilityCallback, PostDiscussionWithEditor {

    private lateinit var discussionVoteRepo: DiscussionVoteRepo

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var richEditorFragment: RichEditorFragment
    private lateinit var addCommentButton: Button
    private lateinit var commentButtons: LinearLayout

    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentsAdapter: DiscussionCommentAdapter

    private var currentDiscussionCommentsList: List<DiscussionComment> = emptyList()
    var discussionId: Long = 0
    var replyToCommentId = 0L

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_full_page, container, false)
        discussionVoteRepo = DiscussionVoteRepo(
            DatabaseProvider.getDatabase(requireContext()).discussionVoteDao()
        )
        val userDetails: UserDetails? =
            SharedPreferencesRetriever.getDetails<UserDetails>(requireContext(), "UserDetails")
        val userBasics: UserBasics = userDetails?.userBasics ?: UserBasics()

        LoadingSpinner.hideLoadingSpinner(this, requireContext())
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        richEditorFragment = RichEditorFragment(50)
        richEditorFragment.editorVisibilityCallback = this
        richEditorFragment.postDiscussionWithEditor = this

        val tvOutput = view.findViewById<TextView>(R.id.full_content_text)
        Log.d("FragmentFullContent", "Content: ${arguments?.getString("content")}")
        val content = arguments?.getString("content")

        Log.d("FragmentFullContent", "DiscussionId: ${arguments?.getLong("discussionId", 0)}")
        val discussionIdFromItem = arguments?.getLong("discussionId", 0) ?: 0
        discussionId = discussionIdFromItem
        val htmlContent = content ?: "An error happened. Please try again later."
        val spanned: Spanned = fromHtml(htmlContent, requireContext(), tvOutput, 600, 900)
        tvOutput.text = spanned

        commentsRecyclerView = view.findViewById(R.id.comments_recycler_view)
        commentsRecyclerView.layoutManager = LinearLayoutManager(context)

        val discussionCommentRepository = DiscussionCommentRepository()
        val retryHelper = RetryHelper(
            maxRetries = 3,
            initialDelay = 1000L,
            maxDelay = 3000L,
            factor = 2.0
        )

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val commentsToDisplay = retryHelper.retry {
                suspendCancellableCoroutine { continuation ->
                    discussionCommentRepository.getCommentsByDiscussionId(discussionId) { comments ->
                        if (comments != null) {
                            continuation.resume(comments, null)
                        } else {
                            continuation.resume(null, null)
                        }
                    }
                }
            } ?: emptyList()
            sharedViewModel.retrievedDiscussionComments.value = commentsToDisplay
            currentDiscussionCommentsList = commentsToDisplay
            Log.d("DiscussionComments", currentDiscussionCommentsList.toString())
            commentsAdapter =
                DiscussionCommentAdapter(requireContext(), userBasics, commentsToDisplay, onReplyClick = { comment ->
                    showEditor(comment.commentId)
                }, onLikeClick = { discussionIdToUpdate, commentId, liked ->
                    Log.d("DiscussionFeedFragment", "Liking discussion $commentId")
                    run {
                        if (liked) {
                            PostDiscussionVoteService.postDiscussionVote(
                                discussionIdToUpdate,
                                commentId,
                                LIKE,
                                userBasics,
                                discussionVoteRepo,
                                this@FragmentFullContentDiscussion
                            )
                        } else {
                            PostDiscussionVoteService.postDiscussionVote(
                                discussionIdToUpdate,
                                commentId,
                                NONE,
                                userBasics,
                                discussionVoteRepo,
                                this@FragmentFullContentDiscussion
                            )
                        }
                    }
                    true
                }, onDislikeClick = { discussionIdToUpdate, commentId, disliked ->
                    run {
                        if (disliked) {
                            Log.d("DiscussionFeedFragment", "Disliking discussion")
                            PostDiscussionVoteService.postDiscussionVote(
                                discussionIdToUpdate,
                                commentId,
                                DISLIKE,
                                userBasics,
                                discussionVoteRepo,
                                this@FragmentFullContentDiscussion
                            )
                        } else {
                            PostDiscussionVoteService.postDiscussionVote(
                                discussionIdToUpdate,
                                commentId,
                                NONE,
                                userBasics,
                                discussionVoteRepo,
                                this@FragmentFullContentDiscussion
                            )
                        }
                    }
                    true
                }, CoroutineScope(Dispatchers.Main)
                )
            commentsRecyclerView.adapter = commentsAdapter

            Log.d("DiscussionFeedFragment", "Comments: $commentsToDisplay")
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

    override fun postDiscussionComment(discussionComment: DiscussionComment?) {

        val userDetails: UserDetails? =
            SharedPreferencesRetriever.getDetails<UserDetails>(requireContext(), "UserDetails")
        val userBasics: UserBasics = userDetails?.userBasics ?: UserBasics()
        val discussionCommentToPost = DiscussionComment(
            discussionId = discussionId,
            content = richEditorFragment.getContent(),
            userBasics = userBasics,
            replyTo = replyToCommentId,
            level = if (replyToCommentId == 0L) 0 else getDepthLevel(replyToCommentId) + 1
        )

        val discussionCommentRepository = DiscussionCommentRepository()
        discussionCommentRepository.postDiscussionComment(discussionCommentToPost) { postedComment ->
            Toast.makeText(requireContext(), postedComment?.commentId.toString(), Toast.LENGTH_SHORT).show()
            hideEditor()

            if (postedComment != null) {
                val updatedComments = currentDiscussionCommentsList.toMutableList()
                updatedComments.add(postedComment)
                currentDiscussionCommentsList = updatedComments

                // Refresh the adapter with the new list of comments
                commentsAdapter =
                    DiscussionCommentAdapter(requireContext(),
                        userBasics,
                        currentDiscussionCommentsList,
                        onReplyClick = { comment ->
                            showEditor(comment.commentId)
                        },
                        onLikeClick = { discussionIdToUpdate, commentId, liked ->
                            run {
                                if (liked) {
                                    Log.d("DiscussionFeedFragment", "Liking discussion")
                                    PostDiscussionVoteService.postDiscussionVote(
                                        discussionIdToUpdate, commentId, LIKE, userBasics, discussionVoteRepo, this
                                    )
                                } else {
                                    PostDiscussionVoteService.postDiscussionVote(
                                        discussionIdToUpdate, commentId, NONE, userBasics, discussionVoteRepo, this
                                    )
                                }
                            }
                            true
                        },
                        onDislikeClick = { discussionIdToUpdate, commentId, disliked ->
                            run {
                                if (disliked) {
                                    Log.d("DiscussionFeedFragment", "Disliking discussion")
                                    PostDiscussionVoteService.postDiscussionVote(
                                        discussionIdToUpdate, commentId, DISLIKE, userBasics, discussionVoteRepo, this
                                    )
                                } else {
                                    PostDiscussionVoteService.postDiscussionVote(
                                        discussionIdToUpdate, commentId, NONE, userBasics, discussionVoteRepo, this
                                    )
                                }
                            }
                            true
                        },
                        CoroutineScope(Dispatchers.Main)
                    )
                commentsRecyclerView.adapter = commentsAdapter
            }
        }
    }

    private fun getDepthLevel(replyTo: Long): Int {
        val comment = currentDiscussionCommentsList.find { it.commentId == replyTo }
        return comment?.level ?: 0
    }
}