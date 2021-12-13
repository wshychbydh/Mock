package com.cool.eye.mock

/**
 * Created by ycb on 6/18/21
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MOCK(
    val method: String = "",
    val url: String = "",
    val delay: Long = 0L
)
