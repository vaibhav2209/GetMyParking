package com.example.getmyparking.network

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonRequest
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


class GsonRequest<T> private constructor(
    private val httpRequestType: Int,
    private val urls: String,
    private val requestBody: String?,
    private val clazz: Class<T>,
    private val header: Map<String?, String?>?,
    private val listener: Response.Listener<T>,
    private val errorListeners: Response.ErrorListener
) : JsonRequest<T>(httpRequestType, urls, requestBody, listener, errorListeners) {

    private val gson = Gson()

    companion object{

        fun <T> getGsonRequest(
            requestType: RequestType,
            url: String,
            requestBody: String?,
            clazz: Class<T>,
            header: HashMap<String?, String?>,
            listener: Response.Listener<T>,
            errorListeners: Response.ErrorListener
        ): GsonRequest<T> {

            header["Content-Type"] = "application/json"

            return GsonRequest(
                requestType.httpRequestType,
                url,
                requestBody,
                clazz,
                header,
                listener,
                errorListeners
            )
        }

    }

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String?, String?>? {
        return header ?: super.getHeaders()
    }

    override fun deliverResponse(response: T) {
        listener.onResponse(response)
    }

    override fun parseNetworkError(volleyError: VolleyError?): VolleyError {
        return super.parseNetworkError(volleyError)
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val json = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))
            Response.success(
                gson.fromJson(json, clazz),
                HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        }
    }


}