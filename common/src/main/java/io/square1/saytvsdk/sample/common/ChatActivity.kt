package io.square1.saytvsdk.sample.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.square1.saytvsdk.sample.common.databinding.ActivityChatBinding
import java.util.*

class ChatActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityChatBinding.inflate(layoutInflater, null, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.chatView.init(
            chatId = intent.getIntExtra(KEY_CHAT_ID, -1),
            chatName = intent.getStringExtra(KEY_CHAT_NAME) ?: "",
            chatImage = intent.getParcelableExtra(KEY_CHAT_IMAGE) ?: Uri.EMPTY,
            startDate = intent.getSerializableExtra(KEY_CHAT_START_DATE) as Date,
            endDate = intent.getSerializableExtra(KEY_CHAT_END_DATE) as Date
        )
    }

    companion object {

        const val KEY_CHAT_ID = "key_chat_id"
        const val KEY_CHAT_NAME = "key_chat_name"
        const val KEY_CHAT_IMAGE = "key_chat_image"
        const val KEY_CHAT_START_DATE = "key_chat_start_date"
        const val KEY_CHAT_END_DATE = "key_chat_end_date"

        fun start(
            context: Context,
            chatId: Int,
            chatName: String,
            chatImage: Uri,
            startDate: Date,
            endDate: Date
        ) = Intent(context, ChatActivity::class.java).apply {
            putExtra(KEY_CHAT_ID, chatId)
            putExtra(KEY_CHAT_NAME, chatName)
            putExtra(KEY_CHAT_IMAGE, chatImage)
            putExtra(KEY_CHAT_START_DATE, startDate)
            putExtra(KEY_CHAT_END_DATE, endDate)
        }.let { context.startActivity(it) }
    }
}