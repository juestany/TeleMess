package com.example.telemess.app

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.telemess.service.MissedCallProcessorService
import com.example.telemess.ui.navigation.AppNavigation
import com.example.telemess.ui.theme.TeleMessTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNeededPermissions();
        enableEdgeToEdge()
        setContent {
            TeleMessTheme {
                AppNavigation()
            }
        }
    }

    private fun requestNeededPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CALL_LOG
        )

        ActivityCompat.requestPermissions(
            this,
            permissions,
            100
        )
    }
}