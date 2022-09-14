package ch.unibas.dmi.dbis.collector.core.client.interceptors

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ApiKeyAuth(
    private val location: Location,
    private val paramName: String,
    private val apiKey: String
) : Interceptor {

    enum class Location(val value: String) {

        QUERY("query"),
        HEADER("header"),
        COOKIE("cookie");

    }

    companion object {

        const val COOKIE_LABEL = "Cookie"

    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        request = when (location) {
            Location.QUERY -> {
                val newUrl: HttpUrl = request.url
                    .newBuilder()
                    .addQueryParameter(paramName, apiKey)
                    .build()
                request.newBuilder().url(newUrl.toUrl()).build()
            }

            Location.HEADER -> {
                request.newBuilder().addHeader(paramName, apiKey).build()
            }

            Location.COOKIE -> {
                request.newBuilder().addHeader(COOKIE_LABEL, "$paramName=$apiKey").build()
            }
        }

        return chain.proceed(request)
    }

}
