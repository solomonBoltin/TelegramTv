package com.solomonboltin.telegramtv3.configuration

import android.content.Context
import android.os.Build
import org.drinkless.td.libcore.telegram.TdApi


fun getTdLibParams (applicationContext: Context) = TdApi.TdlibParameters().apply {
    useMessageDatabase = false
    // writable database directory
    databaseDirectory = applicationContext.getDir("tdlib", Context.MODE_PRIVATE).absolutePath
    useSecretChats = false
    apiId = 0 // set your own Telegram API id
    apiHash = "YOUR_TELEGRAM_API_HASH"
    systemLanguageCode = "en"
    deviceModel =  Build.MODEL
    systemVersion = "Unknown"
    applicationVersion = "1.0"
    enableStorageOptimizer = true
    ignoreFileNames = false
}