package com.cool.eye.mock

import android.os.SystemClock
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import java.lang.Exception

/**
 * Created by ycb on 6/18/21
 */
class MockInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {

    val request = chain.request()

    val response: Response = try {
      chain.proceed(request)
    } catch (e: IOException) {
      if (MockHelper.mockAble) {
        Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("Mock api")
            .body("{}".toResponseBody())
            .build()
      } else {
        throw e
      }
    }

    if (!MockHelper.mockAble) return response

    val mocks = MockHelper.mockApis
    if (mocks.isNullOrEmpty()) return response

    val url = request.url.toString()
    val method = request.method

    val mock = mocks.find { it.isMatch(method, url) } ?: return response
    if (mock.delay > 0L) {
      SystemClock.sleep(mock.delay)
    }
    return response.newBuilder()
        .body(mock.response.toResponseBody(response.body?.contentType()))
        .build()
  }
}