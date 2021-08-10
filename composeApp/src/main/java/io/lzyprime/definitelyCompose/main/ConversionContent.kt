package io.lzyprime.definitelyCompose.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun ConversationContent(content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = MaterialTheme.colors.background) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        },
        content = content,
    )
}

@Preview
@Composable
fun ItemConversation() {
    val padding = 4.dp
    val avatarSize = 32.dp
    Card {
        Row(Modifier.padding(padding), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberImagePainter(data = ""),
                contentDescription = null,
                modifier = Modifier.size(avatarSize)
            )
            Spacer(modifier = Modifier.width(padding))
            Column(modifier = Modifier.weight(1f)) {
                Text("title")
                Text("newMessage")
            }
            Spacer(modifier = Modifier.width(padding))
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "hh:mm")
                Text(
                    text = "99+",
                    modifier = Modifier
                        .background(shape = CircleShape, color = Color.Red)
                        .padding(padding)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ConversationContent {}
}