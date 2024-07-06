package com.mruraza.gemini.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mruraza.gemini.ViewModels.ChatViewModel
import com.mruraza.gemini.models.Chat
import com.mruraza.gemini.ui.theme.ModelColor
import com.mruraza.gemini.ui.theme.UserColor

@Composable
fun GeminiApp(modifier: Modifier = Modifier, chatViewModel: ChatViewModel) {
    Column(modifier = modifier) {

        HeaderPart(modifier = Modifier)

        // This can be your chat messages display area
        // (Use LazyColumn for efficient scrolling)

        MessageList(modifier = Modifier.weight(1f), chatViewModel.messageList)


        MessageInputArea(modifier = Modifier, onClick = {
            chatViewModel.sendMessage(it)
        })

    }

}

@Composable
fun HeaderPart(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = "GeminiBot",
            modifier = Modifier.padding(16.dp),
            fontSize = 22.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MessageInputArea(modifier: Modifier = Modifier, onClick: (String) -> Unit) {
    var message by remember {
        mutableStateOf("")
    }
    Row(
        modifier = modifier.padding(12.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = {
                message = it
            },
            label = {
                Text("Enter message")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        IconButton(
            onClick = {
                if(message.isNotEmpty()){
                    onClick(message)
                    message = ""
                }

            }
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "send"
            )
        }

    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messages: List<Chat>) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = true
    ) {
        items(messages.reversed()) {
            MessageRow(it)
        }
    }
}

@Composable
fun MessageRow(it: Chat) {
    val isModel = it.role == "model"
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                    .padding(
                        start = if (isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .clip(RoundedCornerShape(48f))
                    .background(if (isModel) ModelColor else UserColor).padding(8.dp)
            ) {
                Text(
                    text = it.message,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

        }
    }

}
