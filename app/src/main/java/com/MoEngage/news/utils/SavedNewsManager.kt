package com.MoEngage.news.utils

import android.content.Context
import com.MoEngage.news.model.Article
import com.MoEngage.news.model.Source
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object SavedNewsManager {
    private const val SHARED_PREFS_NAME = "SavedNewsPrefs"
    private const val KEY_SAVED_ARTICLES = "saved_articles"

    fun saveArticle(context: Context, article: Article) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val savedArticles = getSavedArticles(context).toMutableList()

        if (!savedArticles.contains(article)) {
            savedArticles.add(article)

            val editor = sharedPreferences.edit()
            val jsonArray = JSONArray()
            for (i in savedArticles.indices) {
                val jsonObject = JSONObject()
                val currentArticle = savedArticles[i]
                jsonObject.put("source_id", currentArticle.source.id)
                jsonObject.put("source_name", currentArticle.source.name)
                jsonObject.put("author", currentArticle.author)
                jsonObject.put("title", currentArticle.title)
                jsonObject.put("description", currentArticle.description)
                jsonObject.put("url", currentArticle.url)
                jsonObject.put("imageUrl", currentArticle.imageUrl)
                jsonObject.put("publishedAt", currentArticle.publishedAt)
                jsonObject.put("content", currentArticle.content)
                jsonArray.put(jsonObject)
            }
            editor.putString(KEY_SAVED_ARTICLES, jsonArray.toString())
            editor.apply()
        }
    }

    fun getSavedArticles(context: Context): List<Article> {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val savedArticles = mutableListOf<Article>()
        val jsonString = sharedPreferences.getString(KEY_SAVED_ARTICLES, null)

        if (!jsonString.isNullOrEmpty()) {
            try {
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val sourceId = jsonObject.getString("source_id")
                    val sourceName = jsonObject.getString("source_name")
                    val author = jsonObject.getString("author")
                    val title = jsonObject.getString("title")
                    val description = jsonObject.getString("description")
                    val url = jsonObject.getString("url")
                    val imageUrl = jsonObject.getString("imageUrl")
                    val publishedAt = jsonObject.getString("publishedAt")
                    val content = jsonObject.getString("content")

                    val source = Source(sourceId, sourceName)
                    val article = Article(source, author, title, description, url, imageUrl, publishedAt, content)
                    savedArticles.add(article)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        return savedArticles
    }


    fun unsaveArticle(context: Context, article: Article) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val savedArticles = getSavedArticles(context).toMutableList()

        val iterator = savedArticles.iterator()
        while (iterator.hasNext()) {
            val savedArticle = iterator.next()
            if (savedArticle == article) {
                iterator.remove()
                break // Assuming articles are unique and there's no need to continue iterating
            }
        }

        val editor = sharedPreferences.edit()
        val jsonArray = JSONArray()
        for (i in savedArticles.indices) {
            val jsonObject = JSONObject()
            val currentArticle = savedArticles[i]
            jsonObject.put("source_id", currentArticle.source.id)
            jsonObject.put("source_name", currentArticle.source.name)
            jsonObject.put("author", currentArticle.author)
            jsonObject.put("title", currentArticle.title)
            jsonObject.put("description", currentArticle.description)
            jsonObject.put("url", currentArticle.url)
            jsonObject.put("imageUrl", currentArticle.imageUrl)
            jsonObject.put("publishedAt", currentArticle.publishedAt)
            jsonObject.put("content", currentArticle.content)
            jsonArray.put(jsonObject)
        }
        editor.putString(KEY_SAVED_ARTICLES, jsonArray.toString())
        editor.apply()
    }

    fun isArticleSaved(context: Context, article: Article): Boolean {
        val savedArticles = getSavedArticles(context)
        return savedArticles.contains(article)
    }



}
