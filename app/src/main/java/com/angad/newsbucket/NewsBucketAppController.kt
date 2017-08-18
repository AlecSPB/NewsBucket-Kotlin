package com.angad.newsbucket

import android.app.Application
import android.support.multidex.MultiDex
import com.angad.newsbucket.networks.INewsBucketApiEndpoint
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import helpers.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by angad.tiwari on 16-Aug-17.
 */
class NewsBucketAppController : Application() {

    internal val newsBucketApiEndpoint: INewsBucketApiEndpoint

    companion object {
        val instance: NewsBucketAppController by lazy { NewsBucketAppController() }
    }

    override fun onCreate() {
        super.onCreate()

        MultiDex.install(this)
        Fresco.initialize(this)
    }

    init {
        val httpLogger = HttpLoggingInterceptor()
        httpLogger.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(httpLogger).build()

        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.APP_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        newsBucketApiEndpoint = retrofit.create(INewsBucketApiEndpoint::class.java)
    }
}