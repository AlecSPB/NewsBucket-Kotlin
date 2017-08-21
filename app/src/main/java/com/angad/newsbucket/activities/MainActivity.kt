package com.angad.newsbucket.activities

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View

import com.angad.newsbucket.R
import com.angad.newsbucket.animations.DetailTransition
import com.angad.newsbucket.fragments.NewsSourceFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.simpleName
    var selectedNavMenuItem: MenuItem? = null
    var drawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setSharedElementEnterTransition(DetailTransition(300, 100))
        }

        setSupportActionBar(toolbar)
        drawerToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.drawer_open, R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)
        drawerToggle?.setDrawerIndicatorEnabled(false)
        drawerToggle?.setHomeAsUpIndicator(R.drawable.icon_menu)
        drawerToggle?.let { drawer_layout.addDrawerListener(ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.drawer_open, R.string.drawer_close)) }
        newsbucket_navview.setNavigationItemSelectedListener(object: NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                selectDrawerItem(item)
                return true
            }
        })

        if(savedInstanceState == null) {
            selectedNavMenuItem = newsbucket_navview.menu.findItem(R.id.nav_bbcnews)
            chooseNewsSource(toadd = true, index = 0)
        }
    }

    private fun chooseNewsSource(toadd:Boolean, index: Int) {
        toolbar_title.setText(selectedNavMenuItem?.title?:"News Bucket")
        val newsSourceFragment = NewsSourceFragment()
        val bundle = Bundle()
        when(index) {
            0 -> bundle.putString("source", "bbc-news")
            1 -> bundle.putString("source", "bloomberg")
            2 -> bundle.putString("source", "buzzfeed")
            3 -> bundle.putString("source", "cnn")
            4 -> bundle.putString("source", "espn")
            5 -> bundle.putString("source", "google-news")
            6 -> bundle.putString("source", "the-economist")
            7 -> bundle.putString("source", "the-times-of-india")
        }
        newsSourceFragment.setArguments(bundle)
        if(toadd) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.content, newsSourceFragment, NewsSourceFragment::class.java.simpleName)
                    .commit()
        } else {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content, newsSourceFragment, NewsSourceFragment::class.java.simpleName)
                    .commit()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun selectDrawerItem(menuitem: MenuItem) {
        selectedNavMenuItem = menuitem;
        var position: Int = 0
        when (menuitem.itemId) {
            R.id.nav_bbcnews -> position = 0
            R.id.nav_bloomberg -> position = 1
            R.id.nav_buzzfeed -> position = 2
            R.id.nav_cnn -> position = 3
            R.id.nav_espn -> position = 4
            R.id.nav_googlenews -> position = 5
            R.id.nav_theeconomist -> position = 6
            R.id.nav_thetimesofindia -> position = 7
        }
        chooseNewsSource(toadd = false, index = position)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.getItemId()) {
            android.R.id.home -> drawer_layout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToggle?.onConfigurationChanged(newConfig)
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
