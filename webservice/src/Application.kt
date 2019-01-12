package com.blaketmorgan.ipapp.webservice

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
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
                val response = queryTmdb(query)

                val parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(response)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                val results = json.array<JsonObject>("results")

                var stringToReturn = "["
                if (results != null) {
                    val i = results.size;
                    var it = 0
                    for (result in results) {
                        val movieId: Int = result["id"] as Int
                        val title: String = "\"" + result["title"] + "\""
                        val posterImageUrl: String =
                            "\"https://image.tmdb.org/t/p/w154" + result["poster_path"] + "\""
                        val popularitySumary: String =
                            "\"" + result["popularity"] + " out of " + result["vote_count"] + "\""
                        val movie =
                            "{\"movie_id\": $movieId, \"title\": $title, \"poster_image_url\": $posterImageUrl, \"popularity_summary\": $popularitySumary}"
                        stringToReturn += movie
                        it++
                        if (it != i && it != 10)
                            stringToReturn += ","
                        if (it == 10)
                            break
                    }
                }
                stringToReturn += "]"

                call.respondText(stringToReturn)
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