package com.MoEngage.news.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MoEngage.news.R
import com.MoEngage.news.databinding.ActivityHomeBinding
import com.MoEngage.news.model.Article
import com.MoEngage.news.model.Source
import com.MoEngage.news.ui.adapter.ArticleAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleAdapter
    private var articleList = mutableListOf<Article>()
    private lateinit var popup : ImageView
    private lateinit var progress_bar : ProgressBar
    private var isLoading = false
    private var currentPage = 1
    private val PAGE_SIZE = 10
    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        recyclerView = findViewById(R.id.recyclerView)
//        setupRecyclerView()
//        fetchArticles()
//        FirebaseApp.initializeApp(this)

        popup = findViewById(R.id.menu)

        popup.setOnClickListener {
            popup()
        }



        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)



        navView.setupWithNavController(navController)

//        progress_bar = findViewById(R.id.main_progress_bar)
//        progress_bar.visibility = View.VISIBLE

    }

//    private fun setupRecyclerView() {
//        articleAdapter = ArticleAdapter(this, articleList, object : ArticleAdapter.OnArticleClickListener {
//            override fun onArticleClick(article: Article) {
//                // Handle article click here
//            }
//        })
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = articleAdapter
//
//    }
//
//    private fun fetchArticles() {
//        val apiUrl = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"
//
//        GlobalScope.launch(Dispatchers.IO) {
//            try {
//                val response = URL(apiUrl).readText()
//                val articles = parseArticles(response)
//                Log.d("BODY","$response")
//                withContext(Dispatchers.Main) {
//                    articleList.clear()
//                    articleList.addAll(articles)
//                    articleAdapter.notifyDataSetChanged()
//                    progress_bar.visibility = View.GONE
//                }
//            } catch (e: Exception) {
//                Log.d("BODY","ERRORIN FETCH")
//                progress_bar.visibility = View.GONE
//            }
//        }
//    }
//
//
//    private fun parseArticles(response: String): List<Article> {
//            val articles = mutableListOf<Article>()
//            try {
//                val jsonResponse = JSONObject(response)
//                val jsonArticles = jsonResponse.optJSONArray("articles")
//
//                jsonArticles?.let {
//                    for (i in 0 until it.length()) {
//                        val jsonArticle = it.getJSONObject(i)
//                        val sourceJson = jsonArticle.optJSONObject("source")
//                        val source = Source(sourceJson?.optString("id"), sourceJson?.optString("name"))
//
//                        val article = Article(
//                            source,
//                            jsonArticle.optString("author"),
//                            jsonArticle.optString("title"),
//                            jsonArticle.optString("description"),
//                            jsonArticle.optString("url"),
//                            jsonArticle.optString("urlToImage"),
//                            jsonArticle.optString("publishedAt"),
//                            jsonArticle.optString("content")
//                        )
//                        articles.add(article)
//
//
//                }
//                }
//
//            } catch (e: JSONException) {
//                Log.e("PARSE_ERROR", "JSONException: ${e.message}", e)
//            } catch (e: Exception) {
//                Log.e("PARSE_ERROR", "Exception occurred while parsing JSON: ${e.message}", e)
//            }
//
//
//            return articles
//        }
//
//
    fun sortByDate(ascending: Boolean) {
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



    private fun popup() {
        val popupMenu = PopupMenu(this, popup)
        popupMenu.inflate(R.menu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {

                R.id.latest -> {
                    sortByDate(false)
                    true
                }
                R.id.oldest -> {
                    sortByDate(true)
                    true
                }


                else -> super.onOptionsItemSelected(item)
            }


        }

        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)
            mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception) {
            Log.e("Main", "Error showing menu icons.", e)
        } finally {
            popupMenu.show()
        }


    }



}
