package com.angad.newsbucket.activities

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.angad.newsbucket.R
import com.angad.newsbucket.animations.DetailTransition
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ProgressBarDrawable
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.interfaces.DraweeController
import com.google.android.gms.appinvite.AppInviteInvitation
import helpers.Constants
import kotlinx.android.synthetic.main.activity_image_zoomable.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by angad.tiwari on 18-Aug-17.
 */
class ImageZoomableActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_zoomable)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setSharedElementEnterTransition(DetailTransition(300, 100))
        }
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.icon_back)
        toolbar_title.setText("${intent?.extras?.getString("title")}")

        var ctrl: DraweeController = Fresco.newDraweeControllerBuilder().setUri(Uri.parse(intent?.extras?.getString("url"))).setTapToRetryEnabled(true).build()
        var genericHeirarchy: GenericDraweeHierarchy = GenericDraweeHierarchyBuilder(resources).setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER).setProgressBarImage(ProgressBarDrawable()).build()
        zoomable_news_img.setController(ctrl)
        zoomable_news_img.setHierarchy(genericHeirarchy)
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
}