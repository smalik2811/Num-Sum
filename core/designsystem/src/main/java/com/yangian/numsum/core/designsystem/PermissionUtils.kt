package com.yangian.numsum.core.designsystem

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.isPermissionDeniedPermanently(permission: String): Boolean {
    return shouldShowRequestPermissionRationale(this as Activity, permission)
}

fun Context.openPermissionSetting() {
    Intent(ACTION_APPLICATION_DETAILS_SETTINGS).also {
        it.data = Uri.fromParts("package", packageName, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(it)
    }
}