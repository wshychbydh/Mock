package com.cool.eye.mock

/**
 * Distinguish by method and url
 * Created by ycb on 6/18/21
 */
data class MockApi(
    val method: String,  //请求方式
    val url: String,     //请求地址
    val response: String, //响应结果
    val delay: Long = 0L  //响应延迟时长
) {

  fun isMatch(method: String, url: String): Boolean {
    if (this.method != method) return false
    return url.contains(this.url)
  }

  fun isValid() = !method.isNullOrEmpty()
      && !url.isNullOrEmpty()
      && !response.isNullOrEmpty()

  override fun hashCode(): Int {
    return method.hashCode() + url.hashCode()
  }

  override fun equals(other: Any?): Boolean {
    if (other == null || other !is MockApi) return false
    return method == other.method
        && url == other.url
        && response == other.response
        && delay == other.delay
  }
}