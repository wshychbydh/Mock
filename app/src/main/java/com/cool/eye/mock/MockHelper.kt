package com.cool.eye.mock

import retrofit2.http.*
import java.lang.reflect.Method
import java.util.concurrent.CopyOnWriteArraySet

/**
 * Created by ycb on 6/18/21
 */
object MockHelper {

  @Volatile
  internal var mockAble: Boolean = false

  internal val mockApis: CopyOnWriteArraySet<MockApi> by lazy { CopyOnWriteArraySet() }

  fun enableMock(mockAble: Boolean) {
    this.mockAble = mockAble
  }

  fun release() {
    mockAble = false
    mockApis.clear()
  }

  /**
   * Analog interface data
   * @param [mock] Simulation API
   */
  fun addMockApi(mock: MockApi) {
    mockApis.add(mock)
  }

  /**
   * Add a data source class,
   * and all the methods in the class that match the HTTP request will be simulated
   *
   * @param [source] data source class
   */
  fun addMockDataSource(source: Any) {
    if (source is MockApi) {
      if (source.isValid()) addMockApi(source)
      return
    }
    source.javaClass.declaredMethods.forEach {
      if (hasHttpAnnotation(it)) {
        val mockApi = parseMethod(source, it)
        if (mockApi?.isValid() == true) {
          addMockApi(mockApi)
        }
      }
    }
  }

  private fun parseMethod(
      target: Any,
      method: Method
  ) = parseMock(target, method)
      ?: parsePost(target, method)
      ?: parseGet(target, method)
      ?: parseHead(target, method)
      ?: parsePut(target, method)
      ?: parsePatch(target, method)
      ?: parseDelete(target, method)
      ?: parseOptions(target, method)
      ?: parseHttp(target, method)

  private fun parseMock(target: Any, method: Method) = try {
    val mock = method.getAnnotation(MOCK::class.java)
    method.isAccessible = true
    val response = method.invoke(target).toString()
    MockApi(
        method = mock.method.toUpperCase(),
        url = mock.url,
        response = response,
        delay = mock.delay
    )
  } catch (ignore: Exception) {
    null
  }

  private fun parsePost(target: Any, method: Method) = try {
    val post = method.getAnnotation(POST::class.java)
    val url = post.value
    method.isAccessible = true
    val response = method.invoke(target).toString()
    MockApi(method = "POST", url = url, response = response)
  } catch (ignore: Exception) {
    null
  }

  private fun parseGet(target: Any, method: Method) = try {
    val get = method.getAnnotation(GET::class.java)
    val url = get.value
    method.isAccessible = true
    val response = method.invoke(target).toString()
    MockApi(method = "GET", url = url, response = response)
  } catch (ignore: Exception) {
    null
  }

  private fun parseHead(target: Any, method: Method) = try {
    val head = method.getAnnotation(HEAD::class.java)
    val url = head.value
    method.isAccessible = true
    val response = method.invoke(target).toString()
    MockApi(method = "HEAD", url = url, response = response)
  } catch (ignore: Exception) {
    null
  }

  private fun parsePut(target: Any, method: Method) = try {
    val put = method.getAnnotation(PUT::class.java)
    val url = put.value
    method.isAccessible = true
    val response = method.invoke(target).toString()
    MockApi(method = "PUT", url = url, response = response)
  } catch (ignore: Exception) {
    null
  }

  private fun parsePatch(target: Any, method: Method) = try {
    val patch = method.getAnnotation(PATCH::class.java)
    val url = patch.value
    method.isAccessible = true
    val response = method.invoke(target).toString()
    MockApi(method = "PATCH", url = url, response = response)
  } catch (ignore: Exception) {
    null
  }

  private fun parseDelete(target: Any, method: Method) = try {
    val delete = method.getAnnotation(DELETE::class.java)
    val url = delete.value
    method.isAccessible = true
    val response = method.invoke(target).toString()
    MockApi(method = "DELETE", url = url, response = response)
  } catch (ignore: Exception) {
    null
  }

  private fun parseOptions(target: Any, method: Method) = try {
    val options = method.getAnnotation(OPTIONS::class.java)
    val url = options.value
    method.isAccessible = true
    val response = method.invoke(target).toString()
    MockApi(method = "OPTIONS", url = url, response = response)
  } catch (ignore: Exception) {
    null
  }

  private fun parseHttp(target: Any, method: Method) = try {
    val http = method.getAnnotation(HTTP::class.java)
    val url = http.path
    method.isAccessible = true
    val response = method.invoke(target).toString()
    MockApi(method = http.method.toUpperCase(), url = url, response = response)
  } catch (ignore: Exception) {
    null
  }

  private fun hasHttpAnnotation(method: Method): Boolean {
    val annotations = method.declaredAnnotations
    val http = annotations.find {
      val clazzName = it.annotationClass.qualifiedName
      clazzName == MOCK::class.java.name
          || clazzName == POST::class.java.name
          || clazzName == GET::class.java.name
          || clazzName == HEAD::class.java.name
          || clazzName == PUT::class.java.name
          || clazzName == PATCH::class.java.name
          || clazzName == DELETE::class.java.name
          || clazzName == OPTIONS::class.java.name
          || clazzName == HTTP::class.java.name
    }
    return http != null
  }
}