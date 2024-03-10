package com.solomonboltin.telegramtv.vms

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AppVM : ViewModel() {
    private val log: Logger = LoggerFactory.getLogger(ClientVM::class.java)
    private val context: Context by KoinJavaComponent.inject(Context::class.java)


    val isTv: Boolean
        get() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)

}