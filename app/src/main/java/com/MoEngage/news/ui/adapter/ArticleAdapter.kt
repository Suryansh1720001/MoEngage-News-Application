package com.MoEngage.news.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.MoEngage.news.R
import com.MoEngage.news.model.Article
import com.MoEngage.news.model.Source
import com.MoEngage.news.ui.activities.ArticleDetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.*
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
//import com.bumptech.glide.request.RequestOptions
//import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import javax.sql.DataSource

class ArticleAdapter(
    private val context: Context,
    private val articleList: List<Article>,
    private val onArticleClickListener: OnArticleClickListener
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articleList[position]

        holder.bind(article, onArticleClickListener)
        holder.itemView.setOnClickListener {
            Log.d("URL BODY","${article.url}")
            val intent = Intent(context, ArticleDetailActivity::class.java)
            intent.putExtra("ARTICLE_URL", article.url) // Pass the URL
            intent.putExtra("ARTICLE_IMAGE", article.imageUrl) // Pass the URL
            intent.putExtra("ARTICLE_CONTENT", article.content)
            intent.putExtra("ARTICLE_AUTHOR", article.author)
            intent.putExtra("ARTICLE_TITLE", article.title)
            intent.putExtra("ARTICLE_SOURCE", article.source.name)

            intent.putExtra("ARTICLE_DATA", article) // Pass the whole Article object

            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val headlineTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val sourceTextView: TextView = itemView.findViewById(R.id.sourceTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.tv_time)
        private val description: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val articleImageView: ImageView = itemView.findViewById(R.id.articleImageView)

        fun bind(article: Article, clickListener: OnArticleClickListener) {


            headlineTextView.text = article.title ?: context.getString(R.string.title_placeholder)
            sourceTextView.text = article.source.name ?: context.getString(R.string.source_placeholder)
            dateTextView.text = formatDate(article.publishedAt)
            description.text = article.description ?: "Description"



            val progressBar: ProgressBar = itemView.findViewById(R.id.Image_progress_bar)
            progressBar.visibility = View.VISIBLE // Show progress bar



            article.imageUrl?.let {
                Glide.with(context)
                    .load(it)
                    .apply(
                        RequestOptions()
                            .error(R.drawable.breaking_news)
                    )
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE // Hide progress bar on failure
                            return false // Return false to allow Glide to handle the failure event
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: com.bumptech.glide.load.DataSource,
                            isFirstResource: Boolean
                        ): Boolean {

                            progressBar.visibility = View.GONE // Hide progress bar on success
                            return false
                        }

                    })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(articleImageView)
            } ?: run {
                articleImageView.setImageResource(R.drawable.breaking_news)
                progressBar.visibility = View.GONE
            }





            itemView.setOnClickListener {
                clickListener.onArticleClick(article)
            }


        }

        private fun formatDate(dateString: String?): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy\nHH:mm:ss", Locale.getDefault())
            val date = dateString?.let {
                inputFormat.parse(it)
            }
            return date?.let {
                outputFormat.format(date)
            } ?: ""
        }
    }

    interface OnArticleClickListener {
        fun onArticleClick(article: Article)
    }


}
