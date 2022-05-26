package io.square1.saytvsdk.sample.firebase

import android.app.Application
import android.util.Log
import io.square1.saytvsdk.SayTVApplication
import io.square1.saytvsdk.SayTVSdk
import io.square1.saytvsdk.app.model.Result
import io.square1.saytvsdk.core.extension.doNothing
import io.square1.saytvsdk.sample.common.logcat

/**
 * Or you can extend our helper class [SayTVApplication] and the init
 * will get called automatically. Please check the project README
 */
class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        SayTVSdk.init(this) {
            when (it) {
                is Result.Success ->
                    logcat { "SayTVSdk initialized successfully" }
                is Result.Error.Firebase ->
                    logcat(Log.ERROR, it.throwable) { "SayTVSdk failed to initialize:" }
                else -> doNothing()
            }
        }
    }

}
