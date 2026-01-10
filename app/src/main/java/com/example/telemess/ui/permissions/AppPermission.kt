package com.example.telemess.ui.permissions

enum class AppPermission(
    val permission: String,
    val label: String
) {
    CALL_HISTORY(
        android.Manifest.permission.READ_CALL_LOG,
        "Allow viewing call history"
    ),
    CALL_STATE(
        android.Manifest.permission.READ_PHONE_STATE,
        "Allow viewing call state"
    ),
    SEND_SMS(
        android.Manifest.permission.SEND_SMS,
        "Allow sending SMS"
    )
}