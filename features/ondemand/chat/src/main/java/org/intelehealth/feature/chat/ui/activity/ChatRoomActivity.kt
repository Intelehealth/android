package org.intelehealth.feature.chat.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.IntentCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.SimpleItemAnimator
import org.intelehealth.app.BuildConfig
import org.intelehealth.core.utils.extensions.setupChatList
import org.intelehealth.core.utils.extensions.showToast
import org.intelehealth.core.utils.extensions.viewModelByFactory
import org.intelehealth.feature.chat.R
import org.intelehealth.feature.chat.data.ChatDataSource
import org.intelehealth.feature.chat.data.ChatRepository
import org.intelehealth.feature.chat.databinding.ActivityChatBinding
import org.intelehealth.feature.chat.room.entity.ChatMessage
import org.intelehealth.feature.chat.model.DayHeader
import org.intelehealth.feature.chat.model.ItemHeader
import org.intelehealth.feature.chat.provider.RetrofitProvider
import org.intelehealth.feature.chat.room.ChatDatabase
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
    private var isMessageSync = false
    private val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    private val chatDatabase: ChatDatabase by lazy {
        ChatDatabase.getInstance(applicationContext)
    }

    private val chatViewModel: ChatViewModel by viewModelByFactory {
        val repository = ChatRepository(
            chatDatabase.chatDao(),
            chatDatabase.chatRoomDao(),
            ChatDataSource(RetrofitProvider.getApiClient(applicationContext))
        )
        ChatViewModel(context = applicationContext, repository = repository)
    }

//    https://dev.intelehealth.org/api/messages/sendMessage
//    eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3MzIxMjczOTksImRhdGEiOnsic2Vzc2lvbklkIjoiQjczMTYyRkYyODFEOUVGQUY1Q0VFRTgyRjFDNjgzMTgiLCJ1c2VySWQiOiIxNDBhZTc1NC05NmQxLTQ3ZmEtYTVlOC1kZDJmZTI0NDc3YjgiLCJuYW1lIjoibWl0aHVubnVyc2UifSwiaWF0IjoxNzMyMTA1NDk1fQ.lMeHUNoK7lceeArEqGnzru5IuJ18e8QRu3HpaRVoGAmqYrwT5k78LgeRNwJ0-akRZb2htqIv3OKqaKoMy__qhYPajcYqs4tdet8bt8NBREznub6gAMg1V5wQiM2N-CE97foKrWu-gZXGxbc5lgXKT-WJSI63x2tXH6wzw7E8sMl5YfeOcLc8ttVRghkfriV63oJzqcu0gI-15k50GBD3Ml-wLPNz5qjkBJop9KbhugVdxHya8zxlWx9kgL8jrQaBV54QpHEU4D0Z78g4E9f68MDydWuyllHbatrNTXLNllS9XjeBjp1ZvEOHB640igOxjrFi3axCQUuN5RalWvWPuw

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        chatViewModel.connect(BuildConfig.SOCKET_URL)
        initChatListView()
        extractData()
        setButtonClickListener()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.title = chatViewModel.roomConfig.patientName
        }
        binding.toolbar.setNavigationOnClickListener { finishAfterTransition() }
    }

    private fun setButtonClickListener() {
        binding.chatContent.etMessageInput.doOnTextChanged { text, _, _, _ ->
            if (chatViewModel.roomConfig.toId.isNotEmpty()) {
                binding.chatContent.btnSendMessage.isEnabled = text?.isNotEmpty() ?: false
            }
        }

        binding.chatContent.btnSendMessage.setOnClickListener {
            val text = binding.chatContent.etMessageInput.text
            chatViewModel.sendMessage(text.toString(), adapter.getLastMessageId() + 1)
            binding.chatContent.etMessageInput.text.clear()
        }
    }

    private fun initChatListView() {
        adapter = ChatMessageAdapter(this, mutableListOf())
        (binding.chatContent.rvConversation.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.chatContent.rvConversation.itemAnimator = null
        binding.chatContent.rvConversation.setupChatList(adapter)
    }

    private fun extractData() {
        intent?.let { data ->
            if (data.hasExtra(EXT_CHAT_ROOM_CONFIG)) {
                IntentCompat.getParcelableExtra(data, EXT_CHAT_ROOM_CONFIG, ChatRoomConfig::class.java)?.let {
                    chatViewModel.roomConfig = it
                    adapter.loginUserId = it.fromId
                    chatViewModel.updateActiveChatRoomDetails()
                    setupActionBar()
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
        chatViewModel.getChatRoomMessages().observe(this) { value ->
           if (value.isNotEmpty()) {
                chatViewModel.roomConfig.toId = getReceiverId(value[value.size - 1])
                binding.chatContent.emptyView.isVisible = false
                bindConversationList(value)
            } else showToast(org.intelehealth.app.R.string.wait_for_the_doctor_message)
        }
    }
//
//    private fun fetchServerMessages(chatMessage: ChatMessage) {
//        isLastMessageFromRemoteUser(chatMessage) {
//            isMessageSync = true
//            chatViewModel.roomConfig.toId = getReceiverId(chatMessage)
//            chatViewModel.loadConversation()
//        }
//    }

    private fun getReceiverId(chatMessage: ChatMessage): String {
        return if (chatMessage.senderId == chatViewModel.roomConfig.fromId) {
            chatMessage.receiverId
        } else chatMessage.senderId
    }

    private fun isLastMessageFromRemoteUser(chatMessage: ChatMessage, block: () -> Unit) {
        if (chatMessage.senderId != chatViewModel.roomConfig.fromId) block.invoke()
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
        binding.chatContent.rvConversation.scrollToPosition(items.size)
    }

//    private fun ackMessageRead(message: ChatMessage) {
//        if (message.senderId != chatViewModel.roomConfig.fromId && message.messageId != 0) {
//            chatViewModel.ackMessageRead(message.messageId)
//        }
//    }

    companion object {
        private const val EXT_CHAT_ROOM_CONFIG = "ext_chat_room_config"
        fun startChatRoomActivity(context: Context, chatRoomConfig: ChatRoomConfig) {
            Intent(context, ChatRoomActivity::class.java).apply {
                putExtra(EXT_CHAT_ROOM_CONFIG, chatRoomConfig)
            }.also { context.startActivity(it) }
        }
    }
}