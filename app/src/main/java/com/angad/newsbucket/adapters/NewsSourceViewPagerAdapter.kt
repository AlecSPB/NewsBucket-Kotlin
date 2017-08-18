package com.angad.newsbucket.adapters

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.angad.newsbucket.fragments.NewsFragment

/**
 * Created by angad.tiwari on 16-Aug-17.
 */
class NewsSourceViewPagerAdapter : FragmentStatePagerAdapter {

    var context: Context
    var source: String

    constructor(context: Context, source: String, fragmentManager: FragmentManager) : super(fragmentManager) {
        this.context = context
        this.source = source
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        fragment = NewsFragment()
        val bundle = Bundle()
        bundle.putString("source", source)

        when(position) {
            0 -> bundle.putString("category", "top")
            1 -> bundle.putString("category", "latest")
            2 -> bundle.putString("category", "popular")
        }
        fragment.setArguments(bundle)

        return fragment ?: NewsFragment();
    }

    override fun getPageTitle(position: Int): CharSequence {
        when(position) {
            0 -> {
                return "top"
            }
            1 -> {
                return "latest"
            }
            2 -> {
                return "popular"
            }
        }
        return ""
    }

    override fun getCount(): Int {
        return 3
    }
}