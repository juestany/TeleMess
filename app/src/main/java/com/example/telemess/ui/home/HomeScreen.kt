package com.example.telemess.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel
) {
    val missedCalls by viewModel.missedCalls.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        /* CONTENT */
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(missedCalls) { call ->
                MissedCallItem(call = call, onRead = viewModel::markAsRead)
            }
        }
    }
}

@Composable
fun MissedCallItem(
    call: HomeMissedCallUi,
    onRead: (Int) -> Unit
) { //TODO: auto-mark as seen when seen

    val formatter = remember {
        SimpleDateFormat("dd MMM • HH:mm", Locale.getDefault())
    }

    val subtitle = when (call.callType) {
        com.example.telemess.data.model.CallType.REJECTED_QUIET_HOURS ->
            "Missed call – Quiet Hours"
        else ->
            "Call rejected"
    }

    Box {
        Card(
            modifier = Modifier.fillMaxWidth().clickable { onRead(call.id) },
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Filled.CallMissed,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(call.phoneNumber, style = MaterialTheme.typography.bodyLarge)
                    Text(subtitle, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        formatter.format(Date(call.timestamp)),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        if (call.isNew) {
            Text(
                text = "NEW",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}