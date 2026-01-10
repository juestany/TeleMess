package com.example.telemess.ui.permissions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel

// adb uninstall com.example.telemess
@Composable
fun PermissionsScreen(
    viewModel: PermissionsViewModel = viewModel()
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        viewModel.refresh() // force recomposition after dialog
    }

    // observe refreshTrigger to recompose
    val _ko = viewModel.refreshTrigger

    Column(modifier = Modifier.padding(16.dp)) {
        PermissionRow(
            text = "Call history viewing allowed",
            permission = Manifest.permission.READ_CALL_LOG,
            launcher = launcher
        )
        PermissionRow(
            text = "Call state viewing allowed",
            permission = Manifest.permission.READ_PHONE_STATE,
            launcher = launcher
        )
        PermissionRow(
            text = "SMS sending allowed",
            permission = Manifest.permission.SEND_SMS,
            launcher = launcher
        )
    }
}

@Composable
fun PermissionRow(
    text: String,
    permission: String,
    launcher: ManagedActivityResultLauncher<String, Boolean>
) {
    val context = LocalContext.current

    val granted = ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                launcher.launch(permission)
            }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = granted,
            onCheckedChange = null,
            enabled = false
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}