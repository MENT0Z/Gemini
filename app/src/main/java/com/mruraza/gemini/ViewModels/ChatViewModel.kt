package com.mruraza.gemini.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.mruraza.gemini.constants.Constant
import com.mruraza.gemini.models.Chat
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val messageList by lazy {
        mutableStateListOf<Chat>()
    }


    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
        apiKey = Constant.key
    )


    fun sendMessage(question: String) {

        //Log.d("MRU",question)

        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.role) { text(it.message) }
                    }.toList()
                )

                messageList.add(Chat(question, "user"))
                messageList.add(Chat("Typing...", "model"))
                val response = chat.sendMessage(question)
                //Log.d("responseGem",response.text.toString())
                messageList.removeLast()
                messageList.add(Chat(response.text.toString().trim(), "model"))
            } catch (e: Exception) {
                messageList.removeLast()
                messageList.add(Chat(e.message.toString(),"model"))
            }
        }
    }

}