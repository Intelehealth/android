package org.intelehealth.feature.chat.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.IntentCompat
import androidx.core.widget.doOnTextChanged
import org.intelehealth.core.utils.extensions.setupLinearView
import org.intelehealth.core.utils.extensions.showToast
import org.intelehealth.feature.chat.R
import org.intelehealth.feature.chat.databinding.ActivityChatBinding
import org.intelehealth.feature.chat.model.ChatMessage
import org.intelehealth.feature.chat.model.DayHeader
import org.intelehealth.feature.chat.model.ItemHeader
import org.intelehealth.feature.chat.ui.adapter.ChatMessageAdapter
import org.intelehealth.feature.chat.ui.viewmodel.ChatViewModel
import org.intelehealth.features.ondemand.mediator.model.ChatRoomConfig
import org.intelehealth.installer.activity.BaseSplitCompActivity

/**
 * Created by Vaghela Mithun R. on 08-11-2024 - 12:33.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ChatRoomActivity : BaseSplitCompActivity() {

    private lateinit var adapter: ChatMessageAdapter
    private val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    private val chatViewModel: ChatViewModel by viewModels<ChatViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.appBar.toolbar)
        binding.appBar.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        initChatListView()
        extractData()
        setButtonClickListener()
    }

    private fun setButtonClickListener() {
        binding.chatContent.etMessageInput.doOnTextChanged { text, _, _, _ ->
            binding.chatContent.btnSendMessage.isEnabled = text?.isNotEmpty() ?: false
        }

        binding.chatContent.btnSendMessage.setOnClickListener {
            val text = binding.chatContent.etMessageInput.text
            sendMessage(text.toString())
        }
    }

    private fun initChatListView() {
        adapter = ChatMessageAdapter(this, mutableListOf())
        binding.chatContent.rvConversation.setupLinearView(adapter)
    }

    private fun extractData() {
        intent?.let { data ->
            if (data.hasExtra(EXT_CHAT_ROOM_CONFIG)) {
                IntentCompat.getParcelableExtra(data, EXT_CHAT_ROOM_CONFIG, ChatRoomConfig::class.java)?.let {
                    chatViewModel.roomConfig = it
                    loadConversation()
                } ?: invalidArguments()
            } else invalidArguments()
        } ?: invalidArguments()
    }

    private fun invalidArguments() {
        showToast(R.string.no_room_found)
        finish()
    }

    private fun loadConversation() {
        chatViewModel.loadConversation().observe(this) {
            chatViewModel.handleResponse(it) { messages -> bindConversationList(messages) }
        }
    }

    private fun bindConversationList(messages: List<ChatMessage>) {
        val items: ArrayList<ItemHeader> = arrayListOf()
        var messageDay = ""
        messages.forEach {
            it.getMessageDay()?.let { msgDay ->
                if (msgDay != messageDay) {
                    items.add(DayHeader.buildHeader(it.createdDate()))
                    messageDay = msgDay
                }
            }
            items.add(it)
        }
        if (::adapter.isInitialized) adapter.updateItems(items)
    }

    private fun sendMessage(message: String) {
        chatViewModel.sendMessage(message)
    }

    companion object {
        private const val EXT_CHAT_ROOM_CONFIG = "ext_chat_room_config"
        fun startChatRoomActivity(context: Context, chatRoomConfig: ChatRoomConfig) {
            Intent(context, ChatRoomActivity::class.java).apply {
                putExtra(EXT_CHAT_ROOM_CONFIG, chatRoomConfig)
            }.also { context.startActivity(it) }
        }
    }
}