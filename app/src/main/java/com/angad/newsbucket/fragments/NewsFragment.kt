package com.angad.newsbucket.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.angad.newsbucket.NewsBucketAppController
import com.angad.newsbucket.R
import com.angad.newsbucket.adapters.NewsAdapter
import com.angad.newsbucket.helpers.Utilities
import com.angad.newsbucket.models.Articles
import com.angad.newsbucket.models.ArticlesBean
import helpers.Constants
import kotlinx.android.synthetic.main.fragment_news.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by angad.tiwari on 16-Aug-17.
 */
class NewsFragment : Fragment() {

    val loadingFilePath = "loading.gif"
    var newsAdapter: NewsAdapter? = null
    var news: MutableList<ArticlesBean>?= null
    var source: String? = null
    var category: String? = null

    companion object {
        val instance: NewsFragment by lazy { NewsFragment() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        source = arguments?.getString("source")
        category = arguments?.getString("category")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Utilities.playGifImages(progress_news_loading, loadingFilePath)

        news = arrayListOf<ArticlesBean>()
        newsAdapter = NewsAdapter(context = context, news = news, category = category, source = source)
        recycler_news.setLayoutManager(LinearLayoutManager(context, RecyclerView.VERTICAL, false))
        recycler_news.setAdapter(newsAdapter)

        getNewsArticles()
    }

    private fun getNewsArticles() {
        if(!Utilities.isNetworkAvailable(context)) {
            progress_news_loading?.setVisibility(View.GONE)
            recycler_news.setVisibility(View.GONE)
            txt_nonews_avail.setText(R.string.msg_no_internet)
            txt_nonews_avail.setVisibility(View.VISIBLE)
            return
        }

        val newsArticlesCall: Call<Articles> = NewsBucketAppController.instance.newsBucketApiEndpoint.getNewsArticles(source = source?:"", sortBy = category?:"", apiKey = Constants.API_KEY)
        newsArticlesCall.enqueue(object : Callback<Articles> {
            override fun onResponse(call: Call<Articles>?, response: Response<Articles>?) {
                progress_news_loading?.setVisibility(View.GONE)
                response?.body()?.let {
                    news?.clear()
                    response?.body()?.articles?.forEach {
                        news?.add(it)
                    }
                    newsAdapter?.notifyDataSetChanged()
                }
                response?.body()?:recycler_news.setVisibility(View.GONE)
                response?.body()?:txt_nonews_avail.setText(JSONObject(response?.errorBody()?.string()).optString("message"))
                response?.body()?:txt_nonews_avail.setVisibility(View.VISIBLE)
            }

            override fun onFailure(call: Call<Articles>?, t: Throwable?) {
                t?.printStackTrace()
                progress_news_loading?.setVisibility(View.GONE)
                recycler_news.setVisibility(View.GONE)
                txt_nonews_avail.setText(t?.localizedMessage)
                txt_nonews_avail.setVisibility(View.VISIBLE)
            }
        })
    }
}