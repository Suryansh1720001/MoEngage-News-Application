    package com.MoEngage.news.ui.activities

    import android.annotation.SuppressLint
    import android.content.Intent
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.fragment.app.Fragment
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.MoEngage.news.R
    import com.MoEngage.news.model.Article
    import com.MoEngage.news.ui.adapter.ArticleAdapter
    import com.MoEngage.news.ui.adapter.SavedNewsAdapter
    import com.MoEngage.news.utils.SavedNewsManager


    import android.app.Activity
    import android.util.Log
    import android.widget.TextView
    import com.airbnb.lottie.LottieAnimationView


    class SavedNews : Fragment() {

        private lateinit var savedArticles: MutableList<Article>
        private lateinit var recyclerView: RecyclerView
        private lateinit var adapter: SavedNewsAdapter

        private val REQUEST_CODE_REFRESH = 101 // Choose your request code


        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_saved_news, container, false)

            val lottieani = view.findViewById<LottieAnimationView>(R.id.lottie_nothing_found)
            val tv_nothing_found = view.findViewById<TextView>(R.id.tv_nothing_found)

            if (SavedNewsManager.isEmpty(requireContext())) {
              lottieani.visibility = View.VISIBLE
                tv_nothing_found.visibility = View.VISIBLE
            } else {
                lottieani.visibility = View.GONE
                tv_nothing_found.visibility = View.GONE
            }

            savedArticles = SavedNewsManager.getSavedArticles(requireContext()).toMutableList()



            recyclerView = view.findViewById(R.id.saved_news_recycle_view)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            adapter = SavedNewsAdapter(savedArticles, requireContext(), object : SavedNewsAdapter.OnArticleClickListener {
                override fun onArticleClick(article: Article) {
                    val intent = Intent(requireContext(), ArticleDetailActivity::class.java)
                    intent.putExtra("ARTICLE_URL", article.url)
                    intent.putExtra("ARTICLE_IMAGE", article.imageUrl)
                    intent.putExtra("ARTICLE_CONTENT", article.content)
                    intent.putExtra("ARTICLE_AUTHOR", article.author)
                    intent.putExtra("ARTICLE_TITLE", article.title)
                    intent.putExtra("ARTICLE_SOURCE", article.source.name)
                    intent.putExtra("ARTICLE_DATA", article)
                    startActivityForResult(intent, REQUEST_CODE_REFRESH)
                }
            })

            recyclerView.adapter = adapter

            return view
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_CODE_REFRESH && resultCode == Activity.RESULT_OK) {
                refreshSavedNews()
            }
        }

        private fun refreshSavedNews() {
            savedArticles.clear()
            savedArticles.addAll(SavedNewsManager.getSavedArticles(requireContext()))
            adapter.notifyDataSetChanged()
        }
    }
