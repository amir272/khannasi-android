package com.manipur.khannasi.adapter

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manipur.khannasi.R
import com.manipur.khannasi.constants.CURRENT_ENV
import com.manipur.khannasi.constants.DISLIKE
import com.manipur.khannasi.constants.LIKE
import com.manipur.khannasi.dto.Article
import com.manipur.khannasi.roomdb.DatabaseProvider
import com.manipur.khannasi.roomrepo.ArticleBookmarkRepo
import com.manipur.khannasi.roomrepo.ArticleVoteRepo
import com.manipur.khannasi.util.CustomDateTimeFormatter.Companion.calculateDateTimeDifference
import com.manipur.khannasi.util.DisplayImage
import com.manipur.khannasi.util.ArticleDiffCallback
import com.manipur.khannasi.util.SpannedHtmlString.Companion.fromHtml
import com.manipur.khannasi.util.CurrentUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleFeedAdapter(
    private val context: Context,
    private val myUserId: Long,
    private var items: List<Article>,
    private val onImageClick: (String?, Int) -> Unit,
    private val onLikeClick: (Long, Boolean) -> Unit,
    private val onBookmarkClick: (Long, Boolean) -> Unit,
    private val onDislikeClick: (Long, Boolean) -> Boolean,
    private val coroutineScope: CoroutineScope,
    private val onItemClicked: (Article, Int) -> Unit
) : RecyclerView.Adapter<ArticleFeedAdapter.HomePageViewHolder>() {

    private val articleBookmarkRepo = ArticleBookmarkRepo(DatabaseProvider.getDatabase(context).articleBookmarkDao())
    private val articleVoteRepo = ArticleVoteRepo(DatabaseProvider.getDatabase(context).articleVoteDao())


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_page, parent, false)
        view.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return HomePageViewHolder(view)
    }

    fun updateData(newItems: List<Article>) {
        val diffCallback = ArticleDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
//        items = newItems
//        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HomePageViewHolder, position: Int) {
        if (holder.adapterPosition == position) {
            holder.bind(items[position])
            holder.itemView.setOnClickListener {
                onItemClicked(items[position], position) // Pass position to the callback
            }
        }
    }

    override fun onViewAttachedToWindow(holder: HomePageViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            holder.bind(items[position])
        }
    }

    override fun onViewDetachedFromWindow(holder: HomePageViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.unbind()
    }

    inner class HomePageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var likeCountValue = 0
        private var dislikeCountValue = 0
        private var bookmarkCountValue = 0

        private val imageView: ImageView = view.findViewById(R.id.image)
        private val headingTextView: TextView = view.findViewById(R.id.heading)
        private val contentTextView: TextView = view.findViewById(R.id.content)
        private val profilePictureTextView: ImageView = view.findViewById(R.id.profile_picture)
        private val publishedDateTextView: TextView = view.findViewById(R.id.date)
        private val usernameTextView: TextView = view.findViewById(R.id.username)
        private val likeCountTextView: TextView = view.findViewById(R.id.like_count)
        private val dislikeCountTextView: TextView = view.findViewById(R.id.dislike_count)
        private val bookmarkCountTextView: TextView = view.findViewById(R.id.bookmark_count)

        private val likeButton: ImageButton = view.findViewById(R.id.like_button)
        private val dislikeButton: ImageButton = view.findViewById(R.id.dislike_button)
        private val bookmarkButton: ImageButton = view.findViewById(R.id.bookmarkButton)

        private val overlayImageDescription: LinearLayout = view.findViewById(R.id.overlayImageDescription)
        private val overlayFullDescription: ConstraintLayout = view.findViewById(R.id.overlayFullDescription)

        private var liked = false
        private var disliked = false
        private var bookmarked = false

        private fun updateLikeDislikeCounts(articleId: Long) {
            coroutineScope.launch {
                Log.d("ArticleFeedAdapter", "article and user ids: $articleId, $myUserId")
                val articleVote = articleVoteRepo.getArticleVoteByPostAndCommentId(articleId, 0, myUserId)
                withContext(Dispatchers.Main) {
                    Log.d("ArticleFeedAdapter", "updateLikeDislikeCounts: $articleVote")
                    articleVote?.let {
                        liked = (it.voteType == LIKE)
                        disliked = (it.voteType == DISLIKE)
                        if (liked) {
                            likedImageResource(likeButton)
                            undislikedImageResource(dislikeButton)
                        } else if (disliked) {
                            dislikedImageResource(dislikeButton)
                            unlikedImageResource(likeButton)
                        } else {
                            unlikedImageResource(likeButton)
                            undislikedImageResource(dislikeButton)
                        }
                    }
                }
            }
        }

        private fun bookmarkCount(articleId: Long) {
            coroutineScope.launch {
                withContext(Dispatchers.Main) {
                    val articleBookmark = articleBookmarkRepo.getArticleBookmarkByArticleId(articleId, myUserId)
                    Log.d("ArticleFeedAdapter", "bookmarkCount: $articleBookmark")
                    if (articleBookmark != null) {
                        bookmarkedImageResource(bookmarkButton)
                        bookmarked = true
                    } else {
                        unbookmarkImageResource(bookmarkButton)
                        bookmarked = false
                    }
                }
            }
        }

        fun bind(article: Article) {
            updateLikeDislikeCounts(article.articleId)
            bookmarkCount(article.articleId)

            likeCountValue = article.likeCount
            dislikeCountValue = article.dislikeCount
            bookmarkCountValue = article.bookmarkCount


            headingTextView.text = article.title
            usernameTextView.text = context.getString(R.string.updated_by, article.author.username)
            article.representativePicture?.let {
                DisplayImage.loadImageFromUrl(
                    context,
                    imageView,
                    CurrentUrl.get(CURRENT_ENV) + it
                )
            }
            val htmlContent = article.content
            val spanned: Spanned = fromHtml(htmlContent, itemView.context, contentTextView, 600, 900)
            Log.d("ArticleFeedAdapter", "bind: $spanned")
            contentTextView.text = spanned
            contentTextView.post {
                if (contentTextView.lineCount > 8) { // Assuming 9 lines is the max visible lines
                    val end = contentTextView.layout.getLineEnd(7) // Get the end of the 7th line
                    val safeEnd = end.coerceAtMost(spanned.length) // Ensure end does not exceed spanned length
                    val truncatedText = SpannableStringBuilder(spanned, 0, safeEnd - 16).append(" ...Tap for more")
                    contentTextView.text = truncatedText
                }
            }
            publishedDateTextView.text = article.publishedDate?.let { calculateDateTimeDifference(it) }
//            profilePictureTextView.setImageDrawable(article.author.profilePictureUrl?.let {
//                DisplayImage.getDrawableFromPath(context,
//                    it
//                )
//            })

            imageView.setOnClickListener {
                onImageClick(article.representativePicture, adapterPosition)
            }

            overlayFullDescription.setOnClickListener {
                onItemClicked(article, position)
            }

            itemView.setOnClickListener {
                onItemClicked(article, position)
            }

            contentTextView.setOnClickListener {
                onItemClicked(article, position)
            }

            displayCount(likeCountTextView, article.likeCount)
            displayCount(dislikeCountTextView, article.dislikeCount)
            displayCount(bookmarkCountTextView, article.bookmarkCount)


            likeButton.setOnClickListener {
                liked = !liked
                if (liked) {
                    if (disliked) {
                        disliked = false
                        dislikeCountValue--
                    }
                    likeCountValue++
                    likedImageResource(likeButton)
                    undislikedImageResource(dislikeButton)
                } else {
                    likeCountValue--
                    unlikedImageResource(imageButton = likeButton)
                    undislikedImageResource(imageButton = dislikeButton)
                }
                displayCount(likeCountTextView, likeCountValue)
                displayCount(dislikeCountTextView, dislikeCountValue)
                onLikeClick(article.articleId, liked)
            }
            dislikeButton.setOnClickListener {
                disliked = !disliked
                if (disliked) {
                    if (liked) {
                        liked = false
                        likeCountValue--
                    }
                    dislikeCountValue++
                    dislikedImageResource(dislikeButton)
                    unlikedImageResource(likeButton)
                } else {
                    dislikeCountValue--
                    undislikedImageResource(dislikeButton)
                    unlikedImageResource(likeButton)
                }
                displayCount(dislikeCountTextView, dislikeCountValue)
                displayCount(likeCountTextView, likeCountValue)
                onDislikeClick(article.articleId, disliked)
            }

            bookmarkButton.setOnClickListener {
                bookmarked = !bookmarked
                if (bookmarked) {
                    bookmarkCountValue++
                    bookmarkedImageResource(bookmarkButton)
                } else {
                    bookmarkCountValue--
                    unbookmarkImageResource(bookmarkButton)
                }
                displayCount(bookmarkCountTextView, bookmarkCountValue)
                onBookmarkClick(article.articleId, bookmarked)
            }
        }

        private fun displayCount(textView: TextView, count: Int) {
            textView.text = context.getString(R.string.put_just_value, count.toString())
        }

        private fun likedImageResource(imageButton: ImageButton) {
            imageButton.setImageResource(R.drawable.baseline_keyboard_double_arrow_up_24_blue)
        }

        private fun unlikedImageResource(imageButton: ImageButton) {
            imageButton.setImageResource(R.drawable.baseline_keyboard_double_arrow_up_24)
        }

        private fun bookmarkedImageResource(imageButton: ImageButton) {
            imageButton.setImageResource(R.drawable.baseline_bookmark_24_blue)
        }

        private fun unbookmarkImageResource(imageButton: ImageButton) {
            imageButton.setImageResource(R.drawable.baseline_bookmark_24)
        }

        private fun dislikedImageResource(imageButton: ImageButton) {
            imageButton.setImageResource(R.drawable.baseline_keyboard_double_arrow_down_24_blue)
        }

        private fun undislikedImageResource(imageButton: ImageButton) {
            imageButton.setImageResource(R.drawable.baseline_keyboard_double_arrow_down_24)
        }

        fun unbind() {
            // Clear any resources or listeners if needed
            itemView.setOnClickListener(null)
            likeButton.setOnClickListener(null)
            dislikeButton.setOnClickListener(null)
            bookmarkButton.setOnClickListener(null)
        }
    }
}