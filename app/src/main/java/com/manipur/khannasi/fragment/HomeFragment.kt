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
import com.manipur.khannasi.adapter.HomePageAdapter
import com.manipur.khannasi.dto.*
import com.manipur.khannasi.misc.LoadingSpinner
import com.manipur.khannasi.misc.NavigateToFragment.Companion.navigateToFragmentWithAddedBundle
import com.manipur.khannasi.misc.RetrieveDetailsFromSharedPreferences
import com.manipur.khannasi.misc.SharedViewModel
import com.manipur.khannasi.repository.ArticleBookmarkRepository
import com.manipur.khannasi.repository.ArticleRepository
import com.manipur.khannasi.repository.ArticleVoteRepository
import com.manipur.khannasi.util.BOOKMARK
import com.manipur.khannasi.util.DISLIKE
import com.manipur.khannasi.util.LIKE
import kotlinx.coroutines.delay

class HomeFragment : Fragment() {

    private var clickedPosition: Int = RecyclerView.NO_POSITION
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var homePageAdapter: HomePageAdapter
    private lateinit var recyclerView: RecyclerView
    private var articlesToDisplay: List<Article> = emptyList()
    private val articleRepository = ArticleRepository()
    private val articleVoteRepository = ArticleVoteRepository()
    private val articleBookmarkRepository = ArticleBookmarkRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoadingSpinner.hideLoadingSpinner(this, requireContext())
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        if(!sharedViewModel.retrievedArticleList.value.isNullOrEmpty()) {
            Log.d("HomeFragment", "onViewCreated: ${sharedViewModel.retrievedArticleList.value}")
            articlesToDisplay = sharedViewModel.retrievedArticleList.value!!
        } else {
            articleRepository.getAllArticles { retrievedArticles ->
                Log.d("HomeFragment", "onViewCreated: $retrievedArticles")
                sharedViewModel.retrievedArticleList.value = retrievedArticles
                articlesToDisplay = retrievedArticles ?: emptyList()
            }
        }

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        homePageAdapter = HomePageAdapter(
            requireContext(),
            emptyList(),
            onLikeClick = {
                articleId -> postArticleVote(articleId, LIKE)
            },
            onDislikeClick = {
                articleId -> postArticleVote(articleId, DISLIKE)
            },
            onBookmarkClick = {
                articleId -> postArticleVote(articleId, BOOKMARK)
            }
        ) { item, position ->
            clickedPosition = position
            val fullContentFragment = FragmentFullContent()
            val bundle = Bundle()
            bundle.putString("content", item.content)
            fullContentFragment.arguments = bundle
            Log.e("HomeFragment", "onItemClicked: ${fullContentFragment.requireArguments()["content"]}")
            LoadingSpinner.showLoadingSpinner(this, requireContext())
            navigateToFragmentWithAddedBundle(parentFragmentManager, fullContentFragment, R.id.mainFragmentContainer, bundle)
        }
        recyclerView.adapter = homePageAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            LoadingSpinner.showLoadingSpinner(this@HomeFragment, requireContext())
            delay(1000)
            if (articlesToDisplay.isNotEmpty()) {
                LoadingSpinner.hideLoadingSpinner(this@HomeFragment, requireContext())
                Log.d("HomeFragment", "onViewCreated: $articlesToDisplay")
                homePageAdapter.updateData(articlesToDisplay)
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    recyclerView.scrollToPosition(clickedPosition)
                }
            }
            LoadingSpinner.hideLoadingSpinner(this@HomeFragment, requireContext())
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(recyclerView)
        }
    }

    private fun postArticleVote(articleId: Long, likeDislikeBookmark: String):Boolean {
        var posted = false
        val userDetails: UserDetails? = RetrieveDetailsFromSharedPreferences.getDetails<UserDetails>(requireContext(), "UserDetails")
        if (userDetails != null) {
            val userBasics: UserBasics = userDetails.userBasics
            if(likeDislikeBookmark == BOOKMARK) {
                val articleVote = ArticleBookmark(
                    articleId = articleId,
                    user = userBasics
                )
                articleBookmarkRepository.bookmarkArticle(articleVote) {
                    postedBookmark -> if(postedBookmark != null) posted = true
                }
            } else {
                val articleVote = ArticleVote(
                    voteType = likeDislikeBookmark,
                    originalPostId = articleId,
                    commentId = 0,
                    userBasics = userBasics
                )
                articleVoteRepository.postArticleVote(articleVote) { postedVote ->
                    if (postedVote != null) posted = true
                }
            }
        }
        return posted
    }
}