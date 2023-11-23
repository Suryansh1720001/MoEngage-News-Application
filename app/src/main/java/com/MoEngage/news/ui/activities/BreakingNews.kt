package com.MoEngage.news.ui.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MoEngage.news.R
import com.MoEngage.news.model.Article
import com.MoEngage.news.model.Source
import com.MoEngage.news.ui.adapter.ArticleAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class BreakingNews : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleAdapter
    private var articleList = mutableListOf<Article>()
    private lateinit var popup: CardView
    private lateinit var progress_bar: ProgressBar
    private var isLoading = false
    private var currentPage = 1
    private val PAGE_SIZE = 10

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_breaking_news, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        setupRecyclerView(view)
        fetchArticles()
        progress_bar = view.findViewById(R.id.main_progress_bar)
        progress_bar.visibility = View.VISIBLE

        popup = view.findViewById(R.id.cv_filter)

        popup.setOnClickListener {
           showCustomDialog()
        }



        return view
    }

    private fun setupRecyclerView(view: View) {
        articleAdapter = ArticleAdapter(requireContext(), articleList, object : ArticleAdapter.OnArticleClickListener {
            override fun onArticleClick(article: Article) {
                // Handle article click here
            }
        })
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articleAdapter
        }
    }

    private fun fetchArticles() {
        val apiUrl = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = URL(apiUrl).readText()
                val articles = parseArticles(response)
                withContext(Dispatchers.Main) {
                    articleList.clear()
                    articleList.addAll(articles)
                    articleAdapter.notifyDataSetChanged()
                    progress_bar.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e("FETCH_ERROR", "Error fetching articles: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    progress_bar.visibility = View.GONE
                }
            }
        }
    }

    private fun parseArticles(response: String): List<Article> {
        val articles = mutableListOf<Article>()
        try {
            val jsonResponse = JSONObject(response)
            val jsonArticles = jsonResponse.optJSONArray("articles")

            jsonArticles?.let {
                for (i in 0 until it.length()) {
                    val jsonArticle = it.getJSONObject(i)
                    val sourceJson = jsonArticle.optJSONObject("source")
                    val source = Source(sourceJson?.optString("id"), sourceJson?.optString("name"))

                    val article = Article(
                        source,
                        jsonArticle.optString("author"),
                        jsonArticle.optString("title"),
                        jsonArticle.optString("description"),
                        jsonArticle.optString("url"),
                        jsonArticle.optString("urlToImage"),
                        jsonArticle.optString("publishedAt"),
                        jsonArticle.optString("content")
                    )
                    articles.add(article)
                }
            }
        } catch (e: JSONException) {
            Log.e("PARSE_ERROR", "JSONException: ${e.message}", e)
        } catch (e: Exception) {
            Log.e("PARSE_ERROR", "Exception occurred while parsing JSON: ${e.message}", e)
        }
        return articles
    }

    private fun sortByDate(ascending: Boolean) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

        if (ascending) {
            articleList.sortWith(compareBy {
                try {
                    dateFormat.parse(it.publishedAt ?: "")
                } catch (e: Exception) {
                    Date(0)
                }
            })
        } else {
            articleList.sortWith(compareByDescending {
                try {
                    dateFormat.parse(it.publishedAt ?: "")
                } catch (e: Exception) {
                    Date(0)
                }
            })
        }
        articleAdapter.notifyDataSetChanged()
    }



    private fun showCustomDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_box_filter)
        // Set up your dialog content and functionality here

        // Example: Dismiss the dialog when a button is clicked
        val cv_latest = dialog.findViewById<CardView>(R.id.cv_latest)
        val cv_oldest = dialog.findViewById<CardView>(R.id.cv_older)
        cv_latest.setOnClickListener {
            sortByDate(false)
            dialog.dismiss()
        }
        cv_oldest.setOnClickListener {
            sortByDate(true)
            dialog.dismiss()
        }

        dialog.show()
    }

}
