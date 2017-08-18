package com.angad.newsbucket.networks

import com.angad.newsbucket.models.Articles
import com.angad.newsbucket.models.Sources
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by angad.tiwari on 16-Aug-17.
 */
interface INewsBucketApiEndpoint {
    //https://newsapi.org/v1/sources?category=business,entertainment,gaming,general,music,politics,science-and-nature,sport,technology&language=en,de,fr&country=au,de,gb,in,it,us
    @GET("v1/sources")
    fun getNewsSources(@Query("category") category:String? = "", @Query("language") language:String? = "", @Query("country") country:String? = "") : Call<Sources>

    //https://newsapi.org/v1/articles?source=the-next-web&sortBy=top,latest,popular&apiKey=0d1d916fb7154fc6955a453c76f36475
    @GET("v1/articles")
    fun getNewsArticles(@Query("source") source:String, @Query("sortBy") sortBy:String, @Query("apiKey") apiKey:String) : Call<Articles>
}