//package com.skeleton_kotlin.api
//
//import okhttp3.Interceptor
//import okhttp3.Response
//import okhttp3.internal.platform.Platform
//import okhttp3.internal.platform.Platform.INFO
//import org.json.JSONArray
//import org.json.JSONObject
//import java.io.IOException
//import java.nio.charset.Charset
//
//class HttpLoggingInterceptor @JvmOverloads constructor(private val logger: Logger = Logger.DEFAULT) : Interceptor {
//    private val TOP_LEFT_CORNER = '┌'
//    private val MIDDLE_CORNER = '├'
//    private val HORIZONTAL_LINE = '│'
//    private val BOTTOM_LEFT_CORNER = '└'
//
//    private val DOUBLE_DIVIDER = "──────────────────────────────────────────────────────────────────────"
//    private val SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
//
//    private val TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
//    private val MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER
//    private val BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
//
//    @Volatile
//    private var level = Level.NONE
//
//    internal lateinit var log: StringBuilder
//
//    enum class Level {
//        NONE,
//        BASIC,
//        HEADERS,
//        BODY
//    }
//
//    interface Logger {
//        fun log(message: String)
//
//        companion object {
//            val DEFAULT: Logger = object : Logger {
//                override fun log(message: String) {
//                    Platform.get().log(INFO, message, null)
//                }
//            }
//        }
//    }
//
//    /**
//     * Change the level at which this interceptor logs.
//     */
//    fun setLevel(level: Level?): HttpLoggingInterceptor {
//        if (level == null) throw NullPointerException("level == null. Use Level.NONE instead.")
//        this.level = level
//        return this
//    }
//
//    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val level = this.level
//
//        val request = chain.request()
//        if (level == Level.NONE) {
//            return chain.proceed(request)
//        }
//
//        val logBody = level == Level.BODY
//        val logHeaders = logBody || level == Level.HEADERS
//
//        if (logHeaders) {
//            try {
//                log = StringBuilder(TOP_BORDER)
//                log.append("\n" + HORIZONTAL_LINE + "--> " + request.method() + ' '.toString() + request.url())
//                log.append("\n" + MIDDLE_BORDER)
//                val headers = request.headers()
//                var i = 0
//                val count = headers.size()
//                while (i < count) {
//                    val name = headers.name(i)
//                    if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(
//                            name,
//                            ignoreCase = true
//                        )
//                    ) {
//                        log.append("\n" + HORIZONTAL_LINE).append(name).append(": ").append(headers.value(i))
//                    }
//                    i++
//                }
//            } catch (e: Exception) {
//            }
//
//        }
//
//        val response: Response
//        try {
//            response = chain.proceed(request)
//        } catch (e: Exception) {
//            logger.log("<-- HTTP FAILED: $e")
//            throw e
//        }
//
//        val responseBody = response.body()
//
//        if (logHeaders) {
//            val source = responseBody!!.source()
//            source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
//            val buffer = source.buffer()
//            var charset: Charset? = UTF8
//            val contentType = responseBody.contentType()
//            if (contentType != null) {
//                charset = contentType.charset(UTF8)
//            }
//            try {
//                //                log = new StringBuilder(TOP_BORDER);
//                log.append("\n" + MIDDLE_BORDER)
//                log.append("\n" + HORIZONTAL_LINE + "<-- " + response.code() + ' '.toString() + response.request().url())
//                log.append("\n" + MIDDLE_BORDER)
//                val json = buffer.clone().readString(charset!!).trim { it <= ' ' }
//                if (json.startsWith("{")) {
//                    val jsonObject = JSONObject(json)
//                    log.append("\n" + HORIZONTAL_LINE)
//                        .append(jsonObject.toString(2).replace("\n".toRegex(), "\n" + HORIZONTAL_LINE))
//                } else if (json.startsWith("[")) {
//                    val jsonArray = JSONArray(json)
//                    log.append("\n" + HORIZONTAL_LINE)
//                        .append(jsonArray.toString(2).replace("\n".toRegex(), "\n" + HORIZONTAL_LINE))
//                } else
//                    log.append("\n" + HORIZONTAL_LINE).append(json)
//                log.append("\n" + MIDDLE_BORDER)
//                log.append("\n$HORIZONTAL_LINE<-- END ").append(request.method())
//                log.append("\n" + BOTTOM_BORDER)
//                logger.log(log.toString())
//            } catch (e: Exception) {
//            }
//
//        }
//
//        return response
//    }
//
//    companion object {
//
//        private val UTF8 = Charset.forName("UTF-8")
//    }
//}
