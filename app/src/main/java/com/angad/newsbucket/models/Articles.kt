package com.angad.newsbucket.models

/**
 * Created by angad.tiwari on 16-Aug-17.
 */
data class Articles(var status: String,
                    var source: String,
                    var sortBy: String,
                    var code: String,
                    var message: String,
                    var articles: List<ArticlesBean>)

data class ArticlesBean(var author: String,
                        var title: String,
                        var description: String,
                        var url: String,
                        var urlToImage: String,
                        var publishedAt: String)