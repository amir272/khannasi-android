package com.manipur.khannasi.adapter

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manipur.khannasi.R
import com.manipur.khannasi.constants.APPROVE
import com.manipur.khannasi.constants.CURRENT_ENV
import com.manipur.khannasi.dto.Discussion
import com.manipur.khannasi.roomdb.DatabaseProvider
import com.manipur.khannasi.roomrepo.DiscussionBookmarkRepo
import com.manipur.khannasi.roomrepo.DiscussionVoteRepo
import com.manipur.khannasi.util.CustomDateTimeFormatter.Companion.calculateDateTimeDifference
import com.manipur.khannasi.util.DisplayImage
import com.manipur.khannasi.util.SpannedHtmlString.Companion.fromHtml
import com.manipur.khannasi.util.CurrentUrl
import com.manipur.khannasi.util.DiscussionDiffCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscussionFeedAdapter(
    private val context: Context,
    private val myUserId: Long,
    private var items: List<Discussion>,
    private val onImageClick: (String?, Int) -> Unit,
    private val onLikeClick: (Long, Boolean) -> Unit,
    private val onBookmarkClick: (Long, Boolean) -> Unit,
    private val coroutineScope: CoroutineScope,
    private val onItemClicked: (Discussion, Int) -> Unit
) : RecyclerView.Adapter<DiscussionFeedAdapter.HomePageViewHolder>() {

    private val discussionVoteRepo = DiscussionVoteRepo(DatabaseProvider.getDatabase(context).discussionVoteDao())
    private val discussionBookmarkRepo = DiscussionBookmarkRepo(DatabaseProvider.getDatabase(context).discussionBookmarkDao())


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_page_discussion, parent, false)
        view.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return HomePageViewHolder(view)
    }

    fun updateData(newItems: List<Discussion>) {
        val diffCallback = DiscussionDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HomePageViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onItemClicked(items[position], position) // Pass position to the callback
        }
    }

    inner class HomePageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageView: ImageView = view.findViewById(R.id.image)
        private val headingTextView: TextView = view.findViewById(R.id.heading)
        private val contentTextView: TextView = view.findViewById(R.id.content)
        private val profilePictureTextView: ImageView = view.findViewById(R.id.profile_picture)
        private val publishedDateTextView: TextView = view.findViewById(R.id.date)
        private val usernameTextView: TextView = view.findViewById(R.id.username)
        private val approvalCountTextView: TextView = view.findViewById(R.id.follow_count)
        private val bookmarkCountTextView: TextView = view.findViewById(R.id.bookmark_count)

        private val followButton: Button = view.findViewById(R.id.follow_button)
        private val bookmarkButton: ImageButton = view.findViewById(R.id.bookmarkButton)
        private val overlayFullDescription: View = view.findViewById(R.id.overlayFullDescription)

        private var followed = false
        private var bookmarked = false
        private var followCountValue = 0
        private var bookmarkCountValue = 0

        private fun approvalCount(discussionId: Long) {
            coroutineScope.launch {
                withContext(Dispatchers.Main) {
                    val discussionVote = discussionVoteRepo.getDiscussionVoteByPostAndCommentId(discussionId, 0, myUserId)
                    Log.d("DiscussionFeedAdapter", "approvalCount: $discussionVote")
                    if (discussionVote?.voteType == APPROVE) {
                        followedButtonResource(followButton)
                        followed = true
                    } else {
                        unfollowedButtonResource(followButton)
                        followed = false
                    }
                }
            }
        }

        private fun bookmarkCount(discussionId: Long) {
            coroutineScope.launch {
                withContext(Dispatchers.Main) {
                    val discussionBookmark = discussionBookmarkRepo.getDiscussionBookmarkByDiscussionId(discussionId, myUserId)
                    Log.d("ArticleFeedAdapter", "bookmarkCount: $discussionBookmark")
                    if (discussionBookmark != null) {
                        bookmarkedImageResource(bookmarkButton)
                        bookmarked = true
                    } else {
                        unbookmarkImageResource(bookmarkButton)
                        bookmarked = false
                    }
                }
            }
        }

        fun bind(discussion: Discussion) {

            approvalCount(discussion.discussionId)
            bookmarkCount(discussion.discussionId)

            followCountValue = discussion.approvalCount
            bookmarkCountValue = discussion.bookmarkCount

            headingTextView.text = discussion.title
            usernameTextView.text = context.getString(R.string.updated_by, discussion.initiator.username)
            discussion.representativePicture?.let {
                DisplayImage.loadImageFromUrl(
                    context,
                    imageView,
                    CurrentUrl.get(CURRENT_ENV) + it
                )
            }
            val htmlContent = discussion.description
            val spanned: Spanned = fromHtml(htmlContent, itemView.context, contentTextView, 600, 900)
            Log.d("DiscussionFeedAdapter", "bind: $spanned")
            contentTextView.text = spanned
            contentTextView.post {
                if (contentTextView.lineCount > 8) { // Assuming 8 lines is the max visible lines
                    val end = contentTextView.layout.getLineEnd(7) // Get the end of the 7th line
                    val safeEnd = end.coerceAtMost(spanned.length) // Ensure end does not exceed spanned length
                    val truncatedText = SpannableStringBuilder(spanned, 0, safeEnd - 16).append(" ...Tap for more")
                    contentTextView.text = truncatedText
                }
            }
            publishedDateTextView.text = discussion.creationDate?.let { calculateDateTimeDifference(it) }
//            profilePictureTextView.setImageDrawable(discussion.author.profilePictureUrl?.let {
//                DisplayImage.getDrawableFromPath(context,
//                    it
//                )
//            })


            imageView.setOnClickListener {
                onImageClick(discussion.representativePicture, adapterPosition)
            }

            overlayFullDescription.setOnClickListener {
                onItemClicked(discussion, position)
            }


            contentTextView.setOnClickListener {
                onItemClicked(discussion, position)
            }

            displayCount(approvalCountTextView, discussion.approvalCount)
            displayCount(bookmarkCountTextView, discussion.bookmarkCount)

            followButton.setOnClickListener {
                followed = !followed
                if(followed) {
                    followCountValue++
                    followedButtonResource(followButton)
                } else {
                    followCountValue--
                    unfollowedButtonResource(followButton)
                }
                displayCount(approvalCountTextView, followCountValue)
                onLikeClick(discussion.discussionId, followed)
            }

            bookmarkButton.setOnClickListener {
                bookmarked = !bookmarked
                if(bookmarked) {
                    bookmarkCountValue++
                    bookmarkedImageResource(bookmarkButton)
                } else {
                    bookmarkCountValue--
                    unbookmarkImageResource(bookmarkButton)
                }
                displayCount(bookmarkCountTextView, bookmarkCountValue)
                onBookmarkClick(discussion.discussionId, bookmarked)
            }
        }

        private fun displayCount(textView: TextView, count: Int) {
            textView.text = context.getString(R.string.put_just_value, count.toString())
        }

        private fun followedButtonResource(button: Button){
            button.setText(R.string.following)
            button.setTextColor(context.resources.getColor(R.color.blue_500))
        }

        private fun unfollowedButtonResource(button: Button){
            button.setText(R.string.follow)
            button.setTextColor(context.resources.getColor(R.color.black))
        }

        private fun bookmarkedImageResource(imageButton: ImageButton){
            imageButton.setImageResource(R.drawable.baseline_bookmark_24_blue)
        }

        private fun unbookmarkImageResource(imageButton: ImageButton){
            imageButton.setImageResource(R.drawable.baseline_bookmark_24)
        }
    }
}