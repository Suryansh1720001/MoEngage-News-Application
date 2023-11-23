
package com.MoEngage.news.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.MoEngage.news.R
import com.MoEngage.news.model.Article
import com.MoEngage.news.model.Source
import com.MoEngage.news.utils.SavedNewsManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class ArticleDetailActivity : AppCompatActivity() {
    private lateinit var gestureDetector: GestureDetector
    private lateinit var articleURL : String
    private lateinit var title : String
    private lateinit var content : String
    private lateinit var author : String
    private lateinit var image_url : String
    private lateinit var source : String
    private lateinit var article : Article


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        gestureDetector = GestureDetector(this, GestureListener())

        articleURL = intent.getStringExtra("ARTICLE_URL").toString()


        content = intent.getStringExtra("ARTICLE_CONTENT").toString()
        title = intent.getStringExtra("ARTICLE_TITLE").toString()
         author = intent.getStringExtra("ARTICLE_AUTHOR").toString()
       source = intent.getStringExtra("ARTICLE_SOURCE").toString()
     image_url = intent.getStringExtra("ARTICLE_IMAGE").toString()
        article= intent.getParcelableExtra("ARTICLE_DATA")!!

        val content_text = findViewById<TextView>(R.id.tv_content)
        val title_text = findViewById<TextView>(R.id.tv_title)
        val author_text = findViewById<TextView>(R.id.tv_author)
        val source_text = findViewById<TextView>(R.id.tv_source)
        val image = findViewById<ImageView>(R.id.iv_news)

        content_text.text = content
        title_text.text = article?.title
        author_text.text = author
        source_text.text = "Source: $source"

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val button = findViewById<ImageView>(R.id.isSaved)
        val article = createArticleObject()

        // Set the appropriate image based on the article's save status
        if (SavedNewsManager.isArticleSaved(this,article)) {
            // Load the saved icon image
            button.setImageResource(R.drawable.heart)
        } else {
            // Load the unsaved icon image
            button.setImageResource(R.drawable.unheart)
        }



        button.setOnClickListener {
//            saved the news
            val article = createArticleObject()
            if(SavedNewsManager.isArticleSaved(this,article)) {
                SavedNewsManager.unsaveArticle(this, article)
                button.setImageResource(R.drawable.unheart)
            }else{

                SavedNewsManager.saveArticle(this, article)
                button.setImageResource(R.drawable.heart)
            }
        }




       image_url?.let {
            Glide.with(this)
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
                .into(image)
        } ?: run {
           image.setImageResource(R.drawable.breaking_news)
            progressBar.visibility = View.GONE
        }



    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(ev!!)
        return super.dispatchTouchEvent(ev)
    }



    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(
            moveEvent: MotionEvent?,
            downEvent: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffY = moveEvent?.y!! - downEvent.y
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    // Swipe Up detected, transition to ActivityB
                    startActivityBWithTransition()
                    return true
                }
            }
            return false
        }
    }
    private fun startActivityBWithTransition() {
        val intent = Intent(this@ArticleDetailActivity, com.MoEngage.news.ui.activities.WebViewActivity::class.java)
        intent.putExtra("ARTICLE_URL",articleURL)
        startActivity(intent)
        overridePendingTransition(R.drawable.slide_in_up, R.drawable.slide_out_up)
    }

    private fun createArticleObject(): Article {
        // Assuming you have these values available in your context
        val title = article.title
        val author = article.author
        val content =article.content
        val imageUrl = article.imageUrl
        val source = article.source
        val url = article.url
        val des = article.description
        val publishedAt = article.publishedAt


        // Create a Source object
        val sourceObject = Source(article.source.id, article.source.name)

        // Create an Article object using the gathered data
        return Article(sourceObject, author, title, des, url, imageUrl, publishedAt, content)
    }

}
