package com.blaketmorgan.itapp.webservice

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import io.github.cdimascio.dotenv.dotenv
import io.ktor.application.*
import io.ktor.features.CORS
import io.ktor.http.ContentType
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import java.net.URL
import java.net.URLEncoder

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
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
            val stringBuilder = StringBuilder(response)
            val json: JsonObject = parser.parse(stringBuilder) as JsonObject
            val results = json.array<JsonObject>("results")

            var stringToReturn = "["
            if (results != null) {
                val i = results.size
                var iterate = 0
                for (result in results) {
                    val movieId: Int = result["id"] as Int
                    val title: String = "\"" + result["title"] + "\""
                    val posterImageUrl: String =
                        "\"https://image.tmdb.org/t/p/w92" + result["poster_path"] + "\""
                    val popularitySumary: String =
                        "\"" + result["popularity"] + " out of " + result["vote_count"] + "\""
                    val movie =
                        "{\"movie_id\": $movieId, \"title\": $title, \"poster_image_url\": $posterImageUrl, \"popularity_summary\": $popularitySumary}"
                    stringToReturn += movie
                    iterate++
                    if (iterate != i && iterate != 10)
                        stringToReturn += ","
                    if (iterate == 10)
                        break
                }
            }
            stringToReturn += "]"

            call.respondText(stringToReturn)
        }
    }
}

fun queryTmdb(title: String?): String {
    val dotenv = dotenv()
    val apiKey = dotenv["apiKey"]
    val baseUrl = "https://api.themoviedb.org/3/search/movie"
    val query = URLEncoder.encode(title, "UTF-8")
    val requestUrl = "$baseUrl?api_key=$apiKey&include_adult=false&query=$query&region=US&language=en-US"

    return URL(requestUrl).readText()
}

