package com.example.getmyparking.repository

import javax.inject.Inject
import kotlin.jvm.Throws


open class BaseRepository(
) {

    @Inject
    protected lateinit var hmacshA256Generator: HMACSHA256Generator

    @Throws(Exception::class)
    protected fun getAuthorizeHeader(key:String): String =
        hmacshA256Generator.authorizeHeaderGenerator(key)

    protected fun getCurrentTime():String =
        hmacshA256Generator.getCurrentTime()
}