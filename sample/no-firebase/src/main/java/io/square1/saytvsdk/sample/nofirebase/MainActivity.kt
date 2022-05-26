package io.square1.saytvsdk.sample.nofirebase

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import androidx.lifecycle.lifecycleScope
import io.square1.saytvsdk.SayTVSdk
import io.square1.saytvsdk.app.model.Result
import io.square1.saytvsdk.sample.R
import io.square1.saytvsdk.sample.databinding.ActivityMainBinding
import io.square1.saytvsdk.sample.common.logcat
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater, null, false)
    }

    private val startDate: Date = Date()
    private val endDate: Date = Date(Date().time + 3*60*60*1000) // now + 3 hours for example

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        SayTVSdk.initResultLiveData.observe(this) {
            when (it) {
                is Result.Success -> logcat { "SayTVSDK initialized successfully" }
                is Result.Error -> logcat { "SayTVSDK failed to initialize" }
            }
        }

        binding.login.setOnClickListener {
            lifecycleScope.launch {
                when (SayTVSdk.login(binding.loginDigicelIdInput.text.toString().toInt())) {
                    is Result.Success -> MainScope().launch {
                        binding.login.text = getString(R.string.main_label_button_logged_in)
                        binding.chatDetailsContainer.visibility = View.VISIBLE
                        binding.scrollView.post {
                            binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                        }
                    }
                    is Result.Error -> logcat(Log.ERROR) { "Failed to Login: " }
                }
            }
        }

        binding.openChat.setOnClickListener {
            ChatActivity.start(
                context = this,
                chatId = binding.chatIdInput.text.toString().toInt(),
                chatName = binding.chatNameInput.text.toString(),
                chatImage = Uri.parse(binding.chatImageInput.text.toString()),
                startDate = startDate,
                endDate = endDate
            )
        }

    }
}