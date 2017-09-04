package com.angad.newsbucket.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.text.format.DateFormat
import com.angad.newsbucket.NewsBucketAppController
import com.facebook.common.util.UriUtil
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by angad.tiwari on 18-Aug-17.
 */
class Utilities {
    companion object {
        val AM_PM_DATE_FORMAT: String = "dd MMM hh:mm a"
        val HR_24_DATE_FORMAT: String = "dd MMM HH:mm"
        val API_DATE_FORMAT1: String = "yyyy-MM-dd'T'hh:mm:ss'Z'"
        val API_DATE_FORMAT2: String = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'"
        val API_TIMEZONE: String = "GMT"

        fun isNetworkAvailable(context: Context) : Boolean {
            val connManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connManager?.activeNetworkInfo?.state == NetworkInfo.State.CONNECTED
        }

        fun formatDateStringToMillis(dateString: String?): Long {
            val sdf1 = SimpleDateFormat(API_DATE_FORMAT1)
            val sdf2 = SimpleDateFormat(API_DATE_FORMAT2)
            try {
                sdf1.setTimeZone(TimeZone.getTimeZone(API_TIMEZONE))
                return sdf1.parse(dateString).time
            } catch (ex:ParseException){
                sdf2.setTimeZone(TimeZone.getTimeZone(API_TIMEZONE))
                return sdf2.parse(dateString).time
            } catch (nex:NullPointerException) {
                return 0
            }
        }

        fun getDateFormat(context: Context?) : String {
            val is24DateFormat: Boolean = DateFormat.is24HourFormat(context)
            return if (is24DateFormat) HR_24_DATE_FORMAT else AM_PM_DATE_FORMAT
        }

        fun formatDateAndTime(format: String, time: Long) : String {
            val date = Date(time)
            val dateFormat = SimpleDateFormat(format)
            dateFormat.setTimeZone(TimeZone.getDefault())
            val formattedDate = dateFormat.format(date)
            return formattedDate
        }

        fun playGifImages(view: SimpleDraweeView?, filePath: String) {
            val uri = Uri.Builder()
                    .scheme(UriUtil.LOCAL_ASSET_SCHEME)
                    .path(filePath)
                    .build()
            val ctrl = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build()
            view?.setController(ctrl)
        }
    }
}