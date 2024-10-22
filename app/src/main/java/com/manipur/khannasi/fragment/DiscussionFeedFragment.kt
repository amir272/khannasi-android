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
import com.manipur.khannasi.adapter.DiscussionFeedAdapter
import com.manipur.khannasi.constants.APPROVE
import com.manipur.khannasi.constants.BOOKMARK
import com.manipur.khannasi.constants.NONE
import com.manipur.khannasi.dto.Discussion
import com.manipur.khannasi.dto.UserBasics
import com.manipur.khannasi.dto.UserDetails
import com.manipur.khannasi.repository.DiscussionRepository
import com.manipur.khannasi.roomdb.DatabaseProvider
import com.manipur.khannasi.roomrepo.DiscussionBookmarkRepo
import com.manipur.khannasi.roomrepo.DiscussionRepo
import com.manipur.khannasi.roomrepo.DiscussionVoteRepo
import com.manipur.khannasi.service.PostDiscussionBookmarkService
import com.manipur.khannasi.service.PostDiscussionVoteService
import com.manipur.khannasi.util.LoadingSpinner
import com.manipur.khannasi.util.MapToRoomEntity.mapToRoomDiscussion
import com.manipur.khannasi.util.NavigateToFragment.Companion.navigateToFragment
import com.manipur.khannasi.util.NavigateToFragment.Companion.navigateToFragmentWithAddedBundle
import com.manipur.khannasi.util.SharedPreferencesRetriever
import com.manipur.khannasi.util.RetryHelper
import com.manipur.khannasi.util.SharedViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class DiscussionFeedFragment : Fragment() {

    private var clickedPosition: Int = RecyclerView.NO_POSITION
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var discussionFeedAdapter: DiscussionFeedAdapter
    private lateinit var recyclerView: RecyclerView
    private var discussionsToDisplay: List<Discussion> = emptyList()
    private val discussionRepository = DiscussionRepository()

    private lateinit var discussionRepo: DiscussionRepo
    private lateinit var discussionBookmarkRepo: DiscussionBookmarkRepo
    private lateinit var discussionVoteRepo: DiscussionVoteRepo

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

        discussionRepo = DiscussionRepo(DatabaseProvider.getDatabase(requireContext()).discussionDao())
        discussionBookmarkRepo = DiscussionBookmarkRepo(DatabaseProvider.getDatabase(requireContext()).discussionBookmarkDao())
        discussionVoteRepo = DiscussionVoteRepo(DatabaseProvider.getDatabase(requireContext()).discussionVoteDao())

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        discussionFeedAdapter = DiscussionFeedAdapter(
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
            onLikeClick = { discussionId, approved ->
                run {
                    if (approved) {
                        Log.d("DiscussionFeedFragment", "Approving discussion")
                        PostDiscussionVoteService.postDiscussionVote(discussionId, 0, APPROVE, userBasics, discussionVoteRepo, this)
                    } else {
                        PostDiscussionVoteService.postDiscussionVote(discussionId, 0, NONE, userBasics, discussionVoteRepo, this)
                    }
                }
            },
            onBookmarkClick = { discussionId, bookmarked ->
                run {
                    if (bookmarked) {
                        Log.d("DiscussionFeedFragment", "Bookmarking discussion")
                        PostDiscussionBookmarkService.postDiscussionBookmark(discussionId, BOOKMARK, userBasics, discussionBookmarkRepo, this)
                    } else {
                        PostDiscussionBookmarkService.postDiscussionBookmark(discussionId, NONE, userBasics, discussionBookmarkRepo, this)
                    }
                }
            },
            coroutineScope = viewLifecycleOwner.lifecycleScope
        ) { item, position ->
            clickedPosition = position
            val fullContentFragment = FragmentFullContentDiscussion()
            val bundle = Bundle()
            bundle.putString("content", item.description)
            bundle.putLong("discussionId", item.discussionId)
            fullContentFragment.arguments = bundle
            Log.e("DiscussionFeedFragment", "onItemClicked: ${fullContentFragment.requireArguments()["content"]}")
            LoadingSpinner.showLoadingSpinner(this, requireContext())
            navigateToFragmentWithAddedBundle(
                parentFragmentManager,
                fullContentFragment,
                R.id.mainFragmentContainer,
                bundle
            )
        }
        recyclerView.adapter = discussionFeedAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            LoadingSpinner.showLoadingSpinner(this@DiscussionFeedFragment, requireContext())
            Log.d("Bookmarks", discussionBookmarkRepo.getAllDiscussionBookmarks().toString())

            val retryHelper = RetryHelper(
                maxRetries = 3,
                initialDelay = 1000L,
                maxDelay = 3000L,
                factor = 2.0
            )

            discussionsToDisplay = retryHelper.retry {
                suspendCancellableCoroutine { continuation ->
                    discussionRepository.getAllDiscussions { retrievedDiscussions ->
                        if (retrievedDiscussions != null) {
                            sharedViewModel.retrievedDiscussionList.value = retrievedDiscussions
                            sharedViewModel.isDiscussionFeedReceived.value = true
                            continuation.resume(retrievedDiscussions, null)
                        } else {
                            continuation.resume(null, null)
                        }
                    }
                }
            } ?: emptyList()

            if (discussionsToDisplay.isNotEmpty()) {
                discussionFeedAdapter.updateData(discussionsToDisplay)
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    recyclerView.scrollToPosition(clickedPosition)
                }
                val discussionsToInsertToRoomDb = discussionsToDisplay.map { mapToRoomDiscussion(it) }
                if (sharedViewModel.isDiscussionFeedReceived.value == false) discussionRepo.insertDiscussions(discussionsToInsertToRoomDb)
            }
            LoadingSpinner.hideLoadingSpinner(this@DiscussionFeedFragment, requireContext())
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(recyclerView)
        }
    }
}