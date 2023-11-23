package com.MoEngage.news.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.content.Intent

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar

import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.MoEngage.news.R
import com.MoEngage.news.model.Article
import com.MoEngage.news.ui.activities.ArticleDetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

//class SavedNewsAdapter(private val articles: List<Article>) : RecyclerView.Adapter<SavedNewsAdapter.ViewHolder>() {
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        // Initialize views from saved_news_item.xml
//        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
//        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
//        // Add other views as needed
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val article = articles[position]
//        // Set data to views in the ViewHolder
//        holder.titleTextView.text = article.title
//        holder.descriptionTextView.text = article.description
//        // Set other data to respective views
//
//        holder.bind(article, onArticleClickListener)
//        holder.itemView.setOnClickListener {
//            Log.d("URL BODY","${article.url}")
//            val intent = Intent(context, ArticleDetailActivity::class.java)
//            intent.putExtra("ARTICLE_URL", article.url) // Pass the URL
//            intent.putExtra("ARTICLE_IMAGE", article.imageUrl) // Pass the URL
//            intent.putExtra("ARTICLE_CONTENT", article.content)
//            intent.putExtra("ARTICLE_AUTHOR", article.author)
//            intent.putExtra("ARTICLE_TITLE", article.title)
//            intent.putExtra("ARTICLE_SOURCE", article.source.name)
//
//            intent.putExtra("ARTICLE_DATA", article) // Pass the whole Article object
//
//            context.startActivity(intent)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        Log.d("ARTICLE_BODY","${articles.size}")
//        return articles.size
//
//    }
//
//
//    inner class SavedNewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        private val headlineTextView: TextView = itemView.findViewById(R.id.titleTextView)
//        private val sourceTextView: TextView = itemView.findViewById(R.id.sourceTextView)
//        private val dateTextView: TextView = itemView.findViewById(R.id.tv_time)
//        private val description: TextView = itemView.findViewById(R.id.descriptionTextView)
//        private val articleImageView: ImageView = itemView.findViewById(R.id.articleImageView)
//
//        fun bind(article: Article, clickListener: ArticleAdapter.OnArticleClickListener) {
//
//
//            headlineTextView.text = article.title ?: context.getString(R.string.title_placeholder)
//            sourceTextView.text = article.source.name ?: context.getString(R.string.source_placeholder)
//            dateTextView.text = formatDate(article.publishedAt)
//            description.text = article.description ?: "Description"
//
//
//
//            val progressBar: ProgressBar = itemView.findViewById(R.id.Image_progress_bar)
//            progressBar.visibility = View.VISIBLE // Show progress bar
//
//
//
//            article.imageUrl?.let {
//                Glide.with(context)
//                    .load(it)
//                    .apply(
//                        RequestOptions()
//                            .error(R.drawable.breaking_news)
//                    )
//                    .listener(object : RequestListener<Drawable> {
//                        override fun onLoadFailed(
//                            e: GlideException?,
//                            model: Any?,
//                            target: Target<Drawable>,
//                            isFirstResource: Boolean
//                        ): Boolean {
//                            progressBar.visibility = View.GONE // Hide progress bar on failure
//                            return false // Return false to allow Glide to handle the failure event
//                        }
//
//                        override fun onResourceReady(
//                            resource: Drawable,
//                            model: Any,
//                            target: Target<Drawable>?,
//                            dataSource: com.bumptech.glide.load.DataSource,
//                            isFirstResource: Boolean
//                        ): Boolean {
//
//                            progressBar.visibility = View.GONE // Hide progress bar on success
//                            return false
//                        }
//
//                    })
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .into(articleImageView)
//            } ?: run {
//                articleImageView.setImageResource(R.drawable.breaking_news)
//                progressBar.visibility = View.GONE
//            }
//
//
//
//
//
//            itemView.setOnClickListener {
//                clickListener.onArticleClick(article)
//            }
//
//
//        }
//
//        private fun formatDate(dateString: String?): String {
//            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
//            val outputFormat = SimpleDateFormat("dd MMM yyyy\nHH:mm:ss", Locale.getDefault())
//            val date = dateString?.let {
//                inputFormat.parse(it)
//            }
//            return date?.let {
//                outputFormat.format(date)
//            } ?: ""
//        }
//    }
//}




class SavedNewsAdapter(private val articles: List<Article>, private val context: Context,
                       private val clickListener: OnArticleClickListener // Add the listener in the constructor
) : RecyclerView.Adapter<SavedNewsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val sourceTextView: TextView = itemView.findViewById(R.id.sourceTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.tv_time)
        val articleImageView: ImageView = itemView.findViewById(R.id.articleImageView)
        val progressBar: ProgressBar = itemView.findViewById(R.id.Image_progress_bar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]

        holder.titleTextView.text = article.title ?: context.getString(R.string.title_placeholder)
        holder.sourceTextView.text = article.source.name ?: context.getString(R.string.source_placeholder)
        holder.descriptionTextView.text = article.description ?: "Description"
        holder.timeTextView.text = formatDate(article.publishedAt)

        holder.progressBar.visibility = View.VISIBLE // Show progress bar


        article.imageUrl?.let {
            Glide.with(context)
                .load(it)
                .apply(RequestOptions().error(R.drawable.breaking_news))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.visibility = View.GONE // Hide progress bar on failure
                        return false // Return false to allow Glide to handle the failure event
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.visibility = View.GONE // Hide progress bar on success
                        return false
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.articleImageView)
        } ?: run {
            holder.articleImageView.setImageResource(R.drawable.breaking_news)
            holder.progressBar.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            // Handle click event if needed
            clickListener.onArticleClick(article)
        }
//
//        holder.itemView.setOnClickListener {
//            Log.d("URL BODY","${article.url}")
//            val intent = Intent(context, ArticleDetailActivity::class.java)
//            intent.putExtra("ARTICLE_URL", article.url) // Pass the URL
//            intent.putExtra("ARTICLE_IMAGE", article.imageUrl) // Pass the URL
//            intent.putExtra("ARTICLE_CONTENT", article.content)
//            intent.putExtra("ARTICLE_AUTHOR", article.author)
//            intent.putExtra("ARTICLE_TITLE", article.title)
//            intent.putExtra("ARTICLE_SOURCE", article.source.name)
//
//            intent.putExtra("ARTICLE_DATA", article) // Pass the whole Article object
//
//            context.startActivity(intent)
//        }


    }

    override fun getItemCount(): Int {
        return articles.size
    }

    private fun formatDate(dateString: String?): String {
        return if (dateString.isNullOrEmpty()) {
            // Handle empty or null date strings
            "No date available" // or any other default value you prefer
        } else {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy\nHH:mm:ss", Locale.getDefault())
            try {
                val date = inputFormat.parse(dateString)
                date?.let {
                    outputFormat.format(date)
                } ?: ""
            } catch (e: ParseException) {
                "Invalid date format" // Handle parsing exceptions
            }
        }
    }

    interface OnArticleClickListener {
        fun onArticleClick(article: Article)
    }


}
