package com.solomonboltin.telegramtv3.configuration

import android.content.Context
import android.os.Build
import org.drinkless.td.libcore.telegram.TdApi


fun getTdLibParams (applicationContext: Context) = TdApi.TdlibParameters().apply {
    useMessageDatabase = true
    // writable database directory
    databaseDirectory = applicationContext.getDir("tdlib", Context.MODE_PRIVATE).absolutePath
    useSecretChats = false
    apiId = 414329
    apiHash = "af17b872310e6467fcd3d0aa82dc0d56"
    systemLanguageCode = "en"
    deviceModel =  Build.MODEL
    systemVersion = "Unknown"
    applicationVersion = "1.0"
    enableStorageOptimizer = true
    ignoreFileNames = false
}