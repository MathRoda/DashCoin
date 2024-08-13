package com.mathroda.profile_screen.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log

// SDK below Tiramisu needs to access the deprecated version
fun Context.getVersionName(): String {
    var packageInfo: PackageInfo? = null
    packageName.let {
        try {
            packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(it, PackageManager.PackageInfoFlags.of(0))
            } else {
                packageManager.getPackageInfo(it, 0)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("DashDebug", e.message.orEmpty())
        }
    }
    return packageInfo?.versionName ?: InvalidVersion
}

private const val InvalidVersion = "x.x.x"