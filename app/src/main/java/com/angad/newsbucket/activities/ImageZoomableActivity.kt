package com.angad.newsbucket.activities

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.angad.newsbucket.R
import com.angad.newsbucket.animations.DetailTransition
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ProgressBarDrawable
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.interfaces.DraweeController
import kotlinx.android.synthetic.main.activity_image_zoomable.*
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}