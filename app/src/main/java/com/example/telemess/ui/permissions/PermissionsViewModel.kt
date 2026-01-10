package com.example.telemess.ui.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class PermissionsViewModel(
    private val context: Context
) : ViewModel() {

    val readCallLogGranted = MutableStateFlow(false)
    val readPhoneStateGranted = MutableStateFlow(false)
    val sendSmsGranted = MutableStateFlow(false)

    fun refresh() {
        readCallLogGranted.value =
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED

        readPhoneStateGranted.value =
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED

        sendSmsGranted.value =
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
    }
}