package com.angad.newsbucket.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import com.angad.newsbucket.NewsBucketAppController

/**
 * Created by angad.tiwari on 18-Aug-17.
 */
class Utilities {
    companion object {
        fun isNetworkAvailable(context: Context) : Boolean {
            val connManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connManager?.activeNetworkInfo?.state == NetworkInfo.State.CONNECTED
        }
    }
}