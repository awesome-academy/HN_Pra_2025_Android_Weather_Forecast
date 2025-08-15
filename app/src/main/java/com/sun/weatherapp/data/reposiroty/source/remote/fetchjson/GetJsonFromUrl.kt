package com.sun.mvp.data.repository.source.remote.fetchjson

import android.os.Handler
import android.os.Looper
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener
import com.sun.weatherapp.utils.Constant
import com.sun.weatherapp.utils.SimpleApiLogger
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class GetJsonFromUrl<T> constructor(
    private val urlString: String,
    private val keyEntity: String,
    private val listener: OnResultListener<T>
) {

    private val mExecutor: Executor = Executors.newSingleThreadExecutor()
    private val mHandler = Handler(Looper.getMainLooper())
    private var data: T? = null

    init {
        callAPI()
    }

    private fun callAPI() {
        mExecutor.execute {
            val startTime = System.currentTimeMillis()
            val fullUrl = urlString + Constant.BASE_API_KEY + Constant.BASE_LANGUAGE

            try {
                val responseJson = getJsonStringFromUrl(fullUrl)
                data = ParseDataWithJson().parseJsonToData(JSONObject(responseJson), keyEntity) as? T

                mHandler.post {
                    data?.let { 
                        listener.onSuccess(it) 
                    } ?: run {
                        val error = Exception("Failed to parse response for keyEntity: $keyEntity")
                        SimpleApiLogger.logError(fullUrl, error, System.currentTimeMillis() - startTime)
                        listener.onError(error)
                    }
                }
            } catch (e: Exception) {
                val duration = System.currentTimeMillis() - startTime
                SimpleApiLogger.logError(fullUrl, e, duration)
                mHandler.post {
                    listener.onError(e)
                }
            }
        }
    }

    private fun getJsonStringFromUrl(urlString: String): String {
        val url = URL(urlString)
        val httpURLConnection = url.openConnection() as HttpURLConnection
        val startTime = System.currentTimeMillis()

        return try {
            httpURLConnection.apply {
                connectTimeout = TIME_OUT
                readTimeout = TIME_OUT
                requestMethod = METHOD_GET
                doOutput = false
            }

            SimpleApiLogger.logRequest(urlString, METHOD_GET)
            SimpleApiLogger.logCurl(urlString, METHOD_GET)

            httpURLConnection.connect()

            val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            bufferedReader.close()

            val responseBody = stringBuilder.toString()
            val duration = System.currentTimeMillis() - startTime

            SimpleApiLogger.logResponse(urlString, httpURLConnection.responseCode, responseBody, duration)

            responseBody

        } catch (e: Exception) {
            val duration = System.currentTimeMillis() - startTime
            SimpleApiLogger.logError(urlString, e, duration)
            throw e
        } finally {
            httpURLConnection.disconnect()
        }
    }

    companion object {
        private const val TIME_OUT = 15000
        private const val METHOD_GET = "GET"
    }
}
