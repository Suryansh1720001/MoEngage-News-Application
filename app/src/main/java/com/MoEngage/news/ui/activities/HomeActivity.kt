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
    private lateinit var popup: ImageView
    private lateinit var progress_bar: ProgressBar
    private var isLoading = false
    private var currentPage = 1
    private val PAGE_SIZE = 10
    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)


        navView.setupWithNavController(navController)

    }

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

}
