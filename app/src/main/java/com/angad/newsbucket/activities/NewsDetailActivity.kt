package com.angad.newsbucket.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.angad.newsbucket.R
import com.angad.newsbucket.animations.DetailTransition
import com.angad.newsbucket.helpers.Utilities
import com.angad.newsbucket.models.ArticlesBean
import com.facebook.datasource.BaseDataSubscriber
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.gms.appinvite.AppInviteInvitation
import com.google.gson.Gson
import helpers.Constants
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

/**
 * Created by angad.tiwari on 18-Aug-17.
 */
class NewsDetailActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = NewsDetailActivity::class.java.simpleName
    var news: ArticlesBean? = null
    var source: String? = null
    var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setSharedElementEnterTransition(DetailTransition(300, 100))
        }
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
        txt_heading?.setText(news?.title?:"No Title")
        if(news?.publishedAt != null) {
            txt_publishdate?.setText(Utilities.formatDateAndTime(Utilities.getDateFormat(this), Utilities.formatDateStringToMillis(news?.publishedAt)))
        } else {
            txt_publishdate?.setText("News date not available")
        }
        img_news?.setImageURI(Uri.parse(news?.urlToImage))
        txt_author.setText(news?.author?:"No Author")
        txt_url.setText(news?.url?:"No Url")
        txt_description.setText(news?.description?:"No Description")

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

    private fun onAppInvite() {
        val intent = AppInviteInvitation.IntentBuilder(getString(R.string.msg_appinvite_title))
                .setMessage(getString(R.string.msg_appinvite_msg))
                .setDeepLink(Uri.parse(getString(R.string.msg_appinvite_link)))
                .setCustomImage(Uri.parse(getString(R.string.msg_appinvite_img)))
                .setCallToActionText(getString(R.string.msg_appinvite_action))
                .build()
        startActivityForResult(intent, Constants.APPINVITE_REQUEST)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
            R.id.appinvite -> onAppInvite()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.img_news -> {
                val intent = Intent(this, ImageZoomableActivity::class.java)
                intent.putExtra("title", "${category} ${source}")
                intent.putExtra("url", news?.urlToImage)
                val pair1: android.support.v4.util.Pair<View, String> = android.support.v4.util.Pair.create(toolbar, resources?.getString(R.string.transition_toolbar))
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