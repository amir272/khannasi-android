package com.manipur.khannasi.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.manipur.khannasi.R
import com.manipur.khannasi.adapter.ArticleFeedAdapter
import com.manipur.khannasi.constants.BOOKMARK
import com.manipur.khannasi.constants.DISLIKE
import com.manipur.khannasi.constants.LIKE
import com.manipur.khannasi.constants.NONE
import com.manipur.khannasi.dto.Article
import com.manipur.khannasi.dto.UserBasics
import com.manipur.khannasi.dto.UserDetails
import com.manipur.khannasi.repository.ArticleRepository
import com.manipur.khannasi.roomdb.DatabaseProvider
import com.manipur.khannasi.roomrepo.ArticleBookmarkRepo
import com.manipur.khannasi.roomrepo.ArticleRepo
import com.manipur.khannasi.roomrepo.ArticleVoteRepo
import com.manipur.khannasi.service.PostArticleBookmarkService
import com.manipur.khannasi.service.PostArticleVoteService
import com.manipur.khannasi.util.LoadingSpinner
import com.manipur.khannasi.util.MapToRoomEntity.mapToRoomArticle
import com.manipur.khannasi.util.NavigateToFragment.Companion.navigateToFragment
import com.manipur.khannasi.util.NavigateToFragment.Companion.navigateToFragmentWithAddedBundle
import com.manipur.khannasi.util.SharedPreferencesRetriever
import com.manipur.khannasi.util.RetryHelper
import com.manipur.khannasi.util.SharedViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class ArticleFeedFragment : Fragment() {

    private var clickedPosition: Int = RecyclerView.NO_POSITION
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var articleFeedAdapter: ArticleFeedAdapter
    private lateinit var recyclerView: RecyclerView
    private var articlesToDisplay: List<Article> = emptyList()
    private val articleRepository = ArticleRepository()

    private lateinit var articleVoteRepo : ArticleVoteRepo
    private lateinit var articleBookmarkRepo: ArticleBookmarkRepo
    private lateinit var articleRepo: ArticleRepo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (clickedPosition != RecyclerView.NO_POSITION) {
            recyclerView.scrollToPosition(clickedPosition)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoadingSpinner.hideLoadingSpinner(this, requireContext())
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val userDetails: UserDetails? =
            SharedPreferencesRetriever.getDetails<UserDetails>(requireContext(), "UserDetails")
        val userBasics: UserBasics = userDetails?.userBasics ?: UserBasics()

        articleVoteRepo = ArticleVoteRepo(DatabaseProvider.getDatabase(requireContext()).articleVoteDao())
        articleRepo = ArticleRepo(DatabaseProvider.getDatabase(requireContext()).articleDao())
        articleBookmarkRepo = ArticleBookmarkRepo(DatabaseProvider.getDatabase(requireContext()).articleBookmarkDao())


        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        articleFeedAdapter = ArticleFeedAdapter(
            requireContext(),
            userBasics.userId,
            emptyList(),
            onImageClick = { it, position ->
                clickedPosition = position
                it?.let { it1 -> ImageViewFragment.newInstance(it1) }?.let { it2 ->
                    navigateToFragment(
                        activity = requireActivity(),
                        it2,
                        R.id.mainFragmentContainer
                    )
                }
            },
            onLikeClick = { articleId, liked ->
                run {
                    if (liked) {
                        Log.d("ArticleFeedFragment", "Liking article")
                        PostArticleVoteService.postArticleVote(articleId, 0, LIKE, userBasics, articleVoteRepo, this)
                    } else {
                        PostArticleVoteService.postArticleVote(articleId, 0, NONE, userBasics, articleVoteRepo, this)
                    }
                }
            },
            onDislikeClick = { articleId, disliked ->
                run {
                    if (disliked) {
                        Log.d("ArticleFeedFragment", "Disliking article")
                        PostArticleVoteService.postArticleVote(articleId, 0, DISLIKE, userBasics, articleVoteRepo, this)
                    } else {
                        PostArticleVoteService.postArticleVote(articleId, 0, NONE, userBasics, articleVoteRepo, this)
                    }
                }
            },
            onBookmarkClick = { articleId, bookmarked ->
                run {
                    if (bookmarked) {
                        Log.d("ArticleFeedFragment", "Bookmarking article")
                        PostArticleBookmarkService.postArticleBookmark(articleId, BOOKMARK, userBasics, articleBookmarkRepo, this)
                    } else {
                        PostArticleBookmarkService.postArticleBookmark(articleId, NONE, userBasics, articleBookmarkRepo, this)
                    }
                }
            },
            coroutineScope = viewLifecycleOwner.lifecycleScope
        ) { item, position ->
            clickedPosition = position
            val fullContentFragment = FragmentFullContentArticle()
            val bundle = Bundle().apply {
                putString("content", item.content)
                putLong("articleId", item.articleId)
            }
            fullContentFragment.arguments = bundle
            Log.e("ArticleFeedFragment", "onItemClicked: ${fullContentFragment.requireArguments()["content"]}")
            Log.e("ArticleFeedFragment", "onItemClicked: ${fullContentFragment.requireArguments()["articleId"]}")
            LoadingSpinner.showLoadingSpinner(this, requireContext())
            navigateToFragmentWithAddedBundle(
                parentFragmentManager,
                fullContentFragment,
                R.id.mainFragmentContainer,
                bundle
            )
        }
        recyclerView.adapter = articleFeedAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            LoadingSpinner.showLoadingSpinner(this@ArticleFeedFragment, requireContext())

            val retryHelper = RetryHelper(
                maxRetries = 3,
                initialDelay = 1000L,
                maxDelay = 3000L,
                factor = 2.0
            )

            articlesToDisplay = retryHelper.retry {
                suspendCancellableCoroutine { continuation ->
                    articleRepository.getAllArticles { retrievedArticles ->
                        if (retrievedArticles != null) {
                            sharedViewModel.retrievedArticleList.value = retrievedArticles
                            sharedViewModel.isArticleFeedReceived.value = true
                            continuation.resume(retrievedArticles, null)
                        } else {
                            continuation.resume(null, null)
                        }
                    }
                }
            } ?: emptyList()

            if (articlesToDisplay.isNotEmpty()) {
                articleFeedAdapter.updateData(articlesToDisplay)
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    recyclerView.scrollToPosition(clickedPosition)
                }

                val articlesToInsertToRoom = articlesToDisplay.map { mapToRoomArticle(it) }
                if(sharedViewModel.isArticleFeedReceived.value == false) articleRepo.insertArticles(articlesToInsertToRoom)
            }
            LoadingSpinner.hideLoadingSpinner(this@ArticleFeedFragment, requireContext())
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(recyclerView)
        }
    }
}