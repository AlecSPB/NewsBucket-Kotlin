package com.angad.newsbucket.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.angad.newsbucket.R
import com.angad.newsbucket.models.ArticlesBean
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by angad.tiwari on 18-Aug-17.
 */
class NewsDetailActivity : AppCompatActivity(), View.OnClickListener {

    var news: ArticlesBean? = null
    var source: String? = null
    var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        setSupportActionBar(toolbar)

        news = Gson().fromJson(intent?.extras?.getString("data"), ArticlesBean::class.java)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.icon_back)

        source = intent?.extras?.getString("source")
        category = intent?.extras?.getString("category")

        toolbar_title.setText("${category} ${source}")
        txt_heading?.setText(news?.title)
        txt_publishdate?.setText("Published at: ${news?.publishedAt}")
        img_news?.setImageURI(Uri.parse(news?.urlToImage))
        txt_author.setText(news?.author)
        txt_url.setText(news?.url)
        txt_description.setText(news?.description)

        img_news?.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.img_news -> {
                val intent = Intent(this, ImageZoomableActivity::class.java)
                intent.putExtra("title", "${category} ${source}")
                intent.putExtra("url", news?.urlToImage)
                val pair1: android.support.v4.util.Pair<View, String> = android.support.v4.util.Pair.create(img_news, resources?.getString(R.string.transition_news_img))
                val option: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair1 )
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, option.toBundle())
                } else {
                    startActivity(intent)
                }
            }
        }
    }
}