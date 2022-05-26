package io.square1.saytvsdk.sample.common

import android.util.Log

fun Any.logcat(
    priority: Int = Log.VERBOSE,
    throwable: Throwable? = null,
    block: () -> String
) {
    val tag = this.javaClass.simpleName
    when (priority) {
        Log.VERBOSE -> Log.v(tag, block())
        Log.DEBUG -> Log.d(tag, block())
        Log.INFO -> Log.i(tag, block())
        Log.WARN -> Log.w(tag, block())
        Log.ERROR -> Log.e(tag, block(), throwable)
    }
}