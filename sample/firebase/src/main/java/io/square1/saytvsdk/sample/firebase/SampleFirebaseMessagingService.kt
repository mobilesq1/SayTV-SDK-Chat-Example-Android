package io.square1.saytvsdk.sample.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import io.square1.saytvsdk.SayTVSdk

class SampleFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        SayTVSdk.firebaseDelegate.onNewToken(token)
    }

}