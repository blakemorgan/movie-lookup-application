package com.blaketmorgan.ipapp.webservice

import io.github.cdimascio.dotenv.dotenv
import io.ktor.application.*
import io.ktor.features.CORS
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.net.URL
import java.net.URLEncoder

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8081) {
        install(CORS) {
            anyHost()
        }
        routing {
            get("/") {
                call.respondText("Go to /movies to search movies", ContentType.Text.Plain)
            }
            get("/movies") {
                val query = call.request.queryParameters["search"] ?: ""
                call.respondText(queryTmdb(query))
            }
        }
    }
    server.start(wait = true)
}

fun queryTmdb(title: String?): String {
    val dotenv = dotenv()
    val apiKey = dotenv["apiKey"]
    val baseUrl = "https://api.themoviedb.org/3/search/movie"
    val query = URLEncoder.encode(title, "UTF-8")
    val requestUrl = "$baseUrl?api_key=$apiKey&include_adult=false&query=$query&region=US&language=en-US"

    return URL(requestUrl).readText()
}