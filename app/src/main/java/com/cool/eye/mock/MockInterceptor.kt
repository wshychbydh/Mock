package com.cool.eye.mock

import android.os.SystemClock
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Created by ycb on 6/18/21
 */
class MockInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {

    val request = chain.request()
    val response = chain.proceed(request)

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