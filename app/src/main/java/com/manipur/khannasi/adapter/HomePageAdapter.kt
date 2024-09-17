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
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manipur.khannasi.R
import com.manipur.khannasi.dto.Article
import com.manipur.khannasi.misc.CustomDateTimeFormatter.Companion.calculateDateTimeDifference
import com.manipur.khannasi.misc.DisplayImage
import com.manipur.khannasi.misc.ItemDiffCallback
import com.manipur.khannasi.misc.SpannedHtmlString.Companion.fromHtml
import com.manipur.khannasi.util.CURRENT_URL_USED

class HomePageAdapter(
    private val context: Context,
    private var items: List<Article>,
    private val onLikeClick: (Long) -> Boolean,
    private val onDislikeClick: (Long) -> Boolean,
    private val onBookmarkClick: (Long) -> Boolean,
    private val onItemClicked: (Article, Int) -> Unit
) : RecyclerView.Adapter<HomePageAdapter.HomePageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_page, parent, false)
        view.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return HomePageViewHolder(view)
    }

    fun updateData(newItems: List<Article>) {
        val diffCallback = ItemDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
//        items = newItems
//        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HomePageViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onItemClicked(items[position], position) // Pass position to the callback
        }
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

        fun bind(article: Article) {

            likeCountValue = article.likeCount
            dislikeCountValue = article.dislikeCount

            headingTextView.text = article.title
            usernameTextView.text = context.getString(R.string.updated_by, article.author.username)
            article.representativePicture?.let {
                DisplayImage.loadImageFromUrl(
                    context,
                    imageView,
                    CURRENT_URL_USED + it
                )
            }
            val htmlContent = article.content
            val spanned: Spanned = fromHtml(htmlContent, itemView.context, contentTextView, 600, 900)
            Log.d("HomePageAdapter", "bind: $spanned")
            contentTextView.text = spanned
            contentTextView.post {
                if (contentTextView.lineCount > 9) { // Assuming 9 lines is the max visible lines
                    val end = contentTextView.layout.getLineEnd(7) // Get the end of the 7th line
                    val truncatedText = SpannableStringBuilder(spanned, 0, end - 16).append(" ...Tap for more")
                    contentTextView.text = truncatedText
                    contentTextView.setOnClickListener {
                        onItemClicked(article, position)
                    }
                } else {
                    contentTextView.setOnClickListener(null)
                }
            }
            publishedDateTextView.text = article.publishedDate?.let { calculateDateTimeDifference(it) }
//            profilePictureTextView.setImageDrawable(article.author.profilePictureUrl?.let {
//                DisplayImage.getDrawableFromPath(context,
//                    it
//                )
//            })

            displayCount(likeCountTextView, article.likeCount)
            displayCount(dislikeCountTextView, article.dislikeCount)
            displayCount(bookmarkCountTextView, article.bookmarkCount)

            itemView.setOnClickListener {
                onItemClicked(article, position)
            }

            likeButton.setOnClickListener {
                likeCountValue++
                displayCount(likeCountTextView, likeCountValue)
                onLikeClick(article.articleId)
            }

            dislikeButton.setOnClickListener {
                dislikeCountValue++
                displayCount(dislikeCountTextView, dislikeCountValue)
                onDislikeClick(article.articleId)
            }

            bookmarkButton.setOnClickListener {
                bookmarkCountValue++
                displayCount(bookmarkCountTextView, bookmarkCountValue)
                onBookmarkClick(article.articleId)
            }
        }

        private fun displayCount(textView: TextView, count: Int) {
            textView.text = context.getString(R.string.put_just_value, count.toString())
        }
    }
}