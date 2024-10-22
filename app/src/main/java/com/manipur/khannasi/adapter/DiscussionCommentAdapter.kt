package com.manipur.khannasi.adapter

import android.content.Context
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.manipur.khannasi.R
import com.manipur.khannasi.dto.DiscussionComment
import com.manipur.khannasi.dto.UserBasics
import com.manipur.khannasi.roomdb.DatabaseProvider
import com.manipur.khannasi.roomrepo.ArticleVoteRepo
import com.manipur.khannasi.roomrepo.DiscussionVoteRepo
import com.manipur.khannasi.util.LikeDislikeHandler
import com.manipur.khannasi.util.SpannedHtmlString.Companion.fromHtml
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DiscussionCommentAdapter(
    private val context: Context,
    private val myUserBasics: UserBasics,
    private val listWithAllComments: List<DiscussionComment>,
    private val onReplyClick: (DiscussionComment) -> Unit,
    private val onLikeClick: (Long, Long, Boolean) -> Boolean,
    private val onDislikeClick: (Long, Long, Boolean) -> Boolean,
    private val coroutineScope: CoroutineScope
) : ListAdapter<DiscussionComment, DiscussionCommentAdapter.DiscussionCommentViewHolder>(DiscussionCommentDiffUtil) {

    private val articleVoteRepo = ArticleVoteRepo(DatabaseProvider.getDatabase(context).articleVoteDao())
    private val discussionVoteRepo = DiscussionVoteRepo(DatabaseProvider.getDatabase(context).discussionVoteDao())

    private val listShowReplies = arrayListOf<Long>()

    init {
        val topLevelComments = listWithAllComments.filter { it.level == 0 && it.replyTo == 0L }
        submitList(topLevelComments)
    }

    fun expandReplies(commentId: Long) {
        if (!listShowReplies.contains(commentId)) {
            listShowReplies.add(commentId)
            val updatedList = currentList.toMutableList()
            val parentIndex = updatedList.indexOfFirst { it.commentId == commentId }
            if (parentIndex != -1) {
                val replies = listWithAllComments.filter { it.replyTo == commentId }
                updatedList.addAll(parentIndex + 1, replies)
                submitList(updatedList)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionCommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return DiscussionCommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiscussionCommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<DiscussionComment>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    inner class DiscussionCommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val content: TextView = view.findViewById(R.id.comment_content)
        private val author: TextView = view.findViewById(R.id.comment_author)
        private val likeButton: ImageButton = view.findViewById(R.id.like_button)
        private val dislikeButton: ImageButton = view.findViewById(R.id.dislike_button)
        private val replyButton: Button = view.findViewById(R.id.replyIcon)
        private val likeCountTextView: TextView = view.findViewById(R.id.like_count)
        private val dislikeCountTextView: TextView = view.findViewById(R.id.dislike_count)
        private val commentContainer: LinearLayout = view.findViewById(R.id.comment_container)
        private val btnMore: Button = view.findViewById(R.id.btn_more)
        private val llReplies: LinearLayoutCompat = view.findViewById(R.id.ll_replies)

        private var likeCountValue = 0
        private var dislikeCountValue = 0
        private var liked = false
        private var disliked = false

        fun bind(comment: DiscussionComment) {


            coroutineScope.launch {
                Log.d("AllComments", discussionVoteRepo.getAllDiscussionVotes().toString())
            }

            likeCountValue = comment.likeCount
            dislikeCountValue = comment.dislikeCount
            val spanned: Spanned = fromHtml(comment.content, context, content, 600, 900)
            content.text = spanned
            author.text = comment.userBasics.username

            LikeDislikeHandler.updateLikeDislikeCounts(
                comment.discussionId,
                comment.commentId,
                myUserBasics.userId,
                articleVoteRepo,
                discussionVoteRepo,
                articleDiscussion="discussion",
                likeButton,
                dislikeButton,
                { liked = it },
                { disliked = it },
                coroutineScope
            )

            // Reset margins for all comments
            val layoutParams = commentContainer.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(comment.level*4, 0, 0, 0)
            commentContainer.layoutParams = layoutParams

            replyButton.setOnClickListener {
                onReplyClick(comment)
            }

            val replies = listWithAllComments.filter { it.replyTo == comment.commentId }
            if (replies.isNotEmpty()) {
                btnMore.isVisible = true
                updateRepliesVisibility(comment, replies)
            } else {
                btnMore.isVisible = false
                llReplies.isVisible = false
            }

            displayCount(likeCountTextView, likeCountValue)
            displayCount(dislikeCountTextView, dislikeCountValue)

            likeButton.setOnClickListener {
                liked = !liked
                val (newLikeCount, newDislikeCount) = LikeDislikeHandler.handleLikeButtonClick(
                    liked,
                    disliked,
                    likeButton,
                    dislikeButton,
                    likeCountValue,
                    dislikeCountValue,
                    { liked -> onLikeClick(comment.discussionId, comment.commentId, liked) }
                )
                likeCountValue = newLikeCount
                dislikeCountValue = newDislikeCount
                displayCount(likeCountTextView, likeCountValue)
                displayCount(dislikeCountTextView, dislikeCountValue)
            }

            dislikeButton.setOnClickListener {
                disliked = !disliked
                val (newLikeCount, newDislikeCount) = LikeDislikeHandler.handleDislikeButtonClick(
                    disliked,
                    liked,
                    likeButton,
                    dislikeButton,
                    likeCountValue,
                    dislikeCountValue,
                    { liked -> onDislikeClick(comment.discussionId, comment.commentId, liked) }
                )
                likeCountValue = newLikeCount
                dislikeCountValue = newDislikeCount
                displayCount(likeCountTextView, likeCountValue)
                displayCount(dislikeCountTextView, dislikeCountValue)
            }

            replyButton.setOnClickListener {
                onReplyClick(comment)
            }
        }

        private fun displayCount(textView: TextView, count: Int) {
            textView.text = context.getString(R.string.put_just_value, count.toString())
        }

        private fun updateRepliesVisibility(comment: DiscussionComment, replies: List<DiscussionComment>) {
            val isExpanded = listShowReplies.contains(comment.commentId)
            btnMore.text = if (isExpanded) "Hide replies" else "Show ${replies.size} replies"
            llReplies.isVisible = isExpanded

            btnMore.setOnClickListener {
                if (!isExpanded) {
                    expandReplies(comment.commentId)
                } else {
                    listShowReplies.remove(comment.commentId)
                    val updatedList = currentList.filter { it.replyTo != comment.commentId }
                    submitList(updatedList)
                }
                updateRepliesVisibility(comment, replies)
            }
        }
    }

    object DiscussionCommentDiffUtil : DiffUtil.ItemCallback<DiscussionComment>() {
        override fun areItemsTheSame(oldItem: DiscussionComment, newItem: DiscussionComment): Boolean {
            return oldItem.commentId == newItem.commentId
        }

        override fun areContentsTheSame(oldItem: DiscussionComment, newItem: DiscussionComment): Boolean {
            return oldItem == newItem
        }
    }
}