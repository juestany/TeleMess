package com.example.telemess.ui.quiet

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.telemess.data.repository.SettingsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuietHoursScreen(
    repository: SettingsRepository
) {
    val factory = remember { QuietHoursViewModelFactory(repository) }
    val viewModel: QuietHoursViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    val smsTemplate by viewModel.smsTemplate.collectAsState()
    val autoSmsEnabled by viewModel.autoSmsEnabled.collectAsState()
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // START TIME
        OutlinedTextField(
            value = String.format("%02d:%02d", uiState.startHour, uiState.startMinute),
            onValueChange = {},
            label = { Text("Start Time") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showStartTimePicker = true
                }
        )

        // END TIME
        OutlinedTextField(
            value = String.format("%02d:%02d", uiState.endHour, uiState.endMinute),
            onValueChange = {},
            label = { Text("End Time") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showEndTimePicker = true
                }
        )

        OutlinedTextField(
            value = smsTemplate,
            onValueChange = viewModel::updateSmsTemplate,
            label = { Text("Enter a message") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = autoSmsEnabled,
                onClick = viewModel::toggleAutoSms
            )
            Text("Send SMS messages")
        }

        // BUTTON
        Button(
            onClick = { viewModel.saveSettings() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Set Quiet Hours")
        }
    }

    val context = LocalContext.current

    // START TIME PICKER
    if (showStartTimePicker) {
        LaunchedEffect(Unit) {
            TimePickerDialog(
                context,
                { _, hour: Int, minute: Int ->
                    viewModel.updateStartTime(hour, minute)
                    showStartTimePicker = false
                },
                uiState.startHour,
                uiState.startMinute,
                true
            ).show()
        }
    }
// TODO: time validation. end time can't be earlier than start time etc
    // END TIME PICKER
    if (showEndTimePicker) {
        LaunchedEffect(Unit) {
            TimePickerDialog(
                context,
                { _, hour: Int, minute: Int ->
                    viewModel.updateEndTime(hour, minute)
                    showEndTimePicker = false
                },
                uiState.endHour,
                uiState.endMinute,
                true
            ).show()
        }
    }
}
