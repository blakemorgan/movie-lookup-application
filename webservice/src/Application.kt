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

// Create the server
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CORS) {
        anyHost()
    }
    routing {
        // Default route
        get("/") {
            call.respondText("Go to /movies to search movies", ContentType.Text.Plain)
        }
        // Movies routh
        get("/movies") {
            // Get search parameter and query TMDb
            val query = call.request.queryParameters["search"] ?: ""
            val response = queryTmdb(query)

            // Parse TMDb JSON response
            val parser = Parser()
            val stringBuilder = StringBuilder(response)
            val json: JsonObject = parser.parse(stringBuilder) as JsonObject
            val results = json.array<JsonObject>("results")

            // Create our custom and filtered JSON response
            var stringToReturn = "["
            if (results != null) {
                val i = results.size
                var iterate = 0

                // For each result in the returned results...
                for (result in results) {

                    // Save keys/values that we need for our JSON response
                    val movieId: Int = result["id"] as Int
                    val title: String = "\"" + result["title"] + "\""
                    val posterImageUrl: String =
                        "\"https://image.tmdb.org/t/p/w92" + result["poster_path"] + "\""
                    val popularitySumary: String =
                        "\"" + result["popularity"] + " out of " + result["vote_count"] + "\""
                    // Build our JSON response
                    val movie =
                        "{\"movie_id\": $movieId, \"title\": $title, \"poster_image_url\": $posterImageUrl, \"popularity_summary\": $popularitySumary}"
                    stringToReturn += movie
                    iterate++

                    // Add commas if necessary
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
    // Build query URL
    val dotenv = dotenv()
    val apiKey = dotenv["apiKey"]
    val baseUrl = "https://api.themoviedb.org/3/search/movie"
    val query = URLEncoder.encode(title, "UTF-8")
    val requestUrl = "$baseUrl?api_key=$apiKey&include_adult=false&query=$query&region=US&language=en-US"

    // Query TMDb and return response
    return URL(requestUrl).readText()
}

