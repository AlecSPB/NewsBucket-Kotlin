package com.angad.newsbucket.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.angad.newsbucket.R
import com.angad.newsbucket.activities.NewsDetailActivity
import com.angad.newsbucket.helpers.Utilities
import com.angad.newsbucket.models.ArticlesBean
import com.facebook.drawee.view.SimpleDraweeView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_news_detail.*

/**
 * Created by angad.tiwari on 16-Aug-17.
 */
class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    var context:Context? = null
    var category:String? = null
    var source:String? = null
    var news:List<ArticlesBean>? = null

    constructor(context:Context, news:List<ArticlesBean>?, category:String?, source:String?) : super() {
        this.context = context
        this.category = category
        this.source = source
        this.news = news
    }

    override fun getItemCount(): Int {
        return news?.size?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater: LayoutInflater? = LayoutInflater.from(context)
        val view: View? = inflater?.inflate(R.layout.adapter_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.txt_heading?.setText(news?.get(position)?.title?:"No Title")
        if(news?.get(position)?.publishedAt != null) {
            holder?.txt_publishdate?.setText(Utilities.formatDateAndTime(Utilities.getDateFormat(context), Utilities.formatDateStringToMillis(news?.get(position)?.publishedAt)))
        } else {
            holder?.txt_publishdate?.setText("News date not available")
        }
        if(news?.get(position)?.urlToImage!=null)
            holder?.img_news?.setImageURI(Uri.parse(news?.get(position)?.urlToImage))
    }

    inner class ViewHolder (view: View?) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val txt_publishdate = view?.findViewById<TextView>(R.id.txt_publishdate) as TextView
        val txt_heading = view?.findViewById<TextView>(R.id.txt_heading) as TextView
        val img_news = view?.findViewById<SimpleDraweeView>(R.id.img_news) as SimpleDraweeView
        val card_news = view?.findViewById<CardView>(R.id.card_news) as CardView

        init {
            card_news.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.card_news -> {
                    val intent = Intent(context, NewsDetailActivity::class.java)
                    intent.putExtra("source", source?.toUpperCase())
                    intent.putExtra("category", category?.toUpperCase())
                    intent.putExtra("data", Gson().toJson(news?.get(adapterPosition)))
                    val pair1: android.support.v4.util.Pair<View, String> = android.support.v4.util.Pair.create(img_news, context?.resources?.getString(R.string.transition_news_img))
                    val pair2: android.support.v4.util.Pair<View, String> = android.support.v4.util.Pair.create(txt_publishdate, context?.resources?.getString(R.string.transition_news_date))
                    val pair3: android.support.v4.util.Pair<View, String> = android.support.v4.util.Pair.create(txt_heading, context?.resources?.getString(R.string.transition_news_heading))
                    val option:ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, pair1, pair2, pair3 )
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        (context as Activity)?.startActivity(intent, option.toBundle())
                    } else {
                        context?.startActivity(intent)
                    }
                }
            }
        }
    }
}