package com.example.getmyparking.repository

import android.annotation.SuppressLint
import android.os.Build
import android.util.Base64
import android.util.Base64.NO_WRAP
import androidx.annotation.RequiresApi
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.jvm.Throws

private const val path = "/parkingdata/v1/parking/marutisuzuki"
private const val tenant = "gmp"

@RequiresApi(Build.VERSION_CODES.N)
class HMACSHA256GeneratorImpl : HMACSHA256Generator {

    private fun createSignature(): String {
        val signature = StringBuilder()
            .append("x-date: ")
            .append(getCurrentTime())
            .append("\n")
            .append("GET ")
            .append(path)
            .append(" HTTP/1.1")
            .append("\n")
            .append("x_gmp_tenant: ")
            .append(tenant)
        Timber.d("ApiCheck: $signature")
        return signature.toString()
    }

    @Throws(Exception::class)
    private fun encode(key: String): String {
        return Mac.getInstance("HmacSHA256")
            .apply { init(SecretKeySpec(key.toByteArray(charset("UTF-8")), "HmacSHA256")) }
            .run { doFinal(createSignature().toByteArray(charset("UTF-8"))) }
            //.run { doFinal(("x-date: Tue, 07 Sep 2021 09:33:51 GMT\nGET /parkingdata/v1/parking/bosch HTTP/1.1\nx_gmp_tenant: gmp").toByteArray(charset("UTF-8"))) }
            .let { Base64.encodeToString(it, NO_WRAP) }
            .apply {
                Timber.tag("ApiCheck").d(" hash: $this")
            }
    }



    @SuppressLint("NewApi", "WeekBasedYear")
    override fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("EEE, dd MMM YYYY HH:mm:ss zzz" , Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        return dateFormat.format(Date())
    }

    @Throws(Exception::class)
    override fun authorizeHeaderGenerator(key: String) =
        StringBuilder()
            .append("hmac username=\"marutisuzuki\", ")
            .append("algorithm=\"hmac-sha256\", ")
            .append("headers=\"x-date request-line x_gmp_tenant\", ")
            .append("signature=\"${encode(key)}\"")
            .toString()


}