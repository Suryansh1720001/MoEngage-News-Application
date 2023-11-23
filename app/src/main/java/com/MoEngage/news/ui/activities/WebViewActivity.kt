package com.MoEngage.news.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import com.MoEngage.news.R

class WebViewActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val articleUrl = intent.getStringExtra("ARTICLE_URL")
        Toast.makeText(this@WebViewActivity,"$articleUrl",Toast.LENGTH_LONG).show()
        val correctedUrl = articleUrl?.replace("http:", "https:") ?: "https://www.example.com"

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)



        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
//        webView.settings.setInitialScale(1)
        webView.clearCache(true)



        val webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.settings?.loadWithOverviewMode = true
                view?.settings?.useWideViewPort = true
//                view?.settings?.setInitialScale(1)
                view?.loadUrl("javascript:(function() { " +
                        "document.body.style.zoom = 1;" +
                        "})()")
                // Page loading is completed here, we can perform further actions if needed
                progressBar.visibility = View.GONE
            }
        }

        webView.webViewClient = webViewClient
        webView.loadUrl(correctedUrl)

    }
}