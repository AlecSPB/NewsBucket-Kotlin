package com.angad.newsbucket.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.angad.newsbucket.R
import com.angad.newsbucket.adapters.NewsSourceViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_news_source.*

/**
 * Created by angad.tiwari on 16-Aug-17.
 */
class NewsSourceFragment : Fragment() {

    var newsSourceViewPagerAdapter: NewsSourceViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_news_source, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsSourceViewPagerAdapter = NewsSourceViewPagerAdapter(context, arguments.getString("source"), fragmentManager)
        pager_newssource.setOffscreenPageLimit(2)
        pager_newssource.setAdapter(newsSourceViewPagerAdapter)
        tabs_newscategory.setupWithViewPager(pager_newssource)
    }
}