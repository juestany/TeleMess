package com.example.telemess.ui.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.telemess.domain.model.IncomingCallEvent
import com.example.telemess.domain.model.MissedCallProcessor
import kotlinx.coroutines.launch

@Composable
fun DebugCallsScreen(processor: MissedCallProcessor) {
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {

        Button(onClick = {
            scope.launch {
                processor.handleCall(
                    IncomingCallEvent(
                        phoneNumber = "+48123123123",
                        wasAnswered = false
                    )
                )
            }
        }) {
            Text("Simulate MISSED call")
        }

        Button(onClick = {
            scope.launch {
                processor.handleCall(
                    IncomingCallEvent(
                        phoneNumber = "+48999888777",
                        wasAnswered = true
                    )
                )
            }
        }) {
            Text("Simulate ANSWERED call")
        }
    }
}