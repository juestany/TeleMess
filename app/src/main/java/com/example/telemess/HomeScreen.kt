package com.example.telemess

import android.Manifest
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat

@Composable
fun HomeScreen() {
    Column {
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            val intent = Intent(context, MessagesCallsService::class.java)
            context.startService(intent)
        }

        Column {
            Text("Welcome home!")
        }
        // ACTION_RESPOND_VIA_MESSAGE
        // SEND_RESPOND_VIA_MESSAGE
        // service: A facility for the application to tell the system about something it wants to be doing in the background
    }
}