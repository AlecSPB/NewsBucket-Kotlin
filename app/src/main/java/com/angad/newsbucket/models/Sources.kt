package com.angad.newsbucket.models

/**
 * Created by angad.tiwari on 16-Aug-17.
 */
data class Sources (var status:String,
                    var sources:List<SourcesBean>)

data class SourcesBean (var id:String,
                        var name:String,
                        var description:String,
                        var url:String,
                        var category:String,
                        var language:String,
                        var country:String,
                        var urlsToLogos:UrlsToLogos,
                        var sortBysAvailable:List<String>)

data class UrlsToLogos (var small:String,
                        var medium:String,
                        var large:String)