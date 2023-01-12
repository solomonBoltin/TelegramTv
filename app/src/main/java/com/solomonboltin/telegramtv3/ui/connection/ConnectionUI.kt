package com.solomonboltin.telegramtv3.ui.connection

import android.graphics.ColorSpace
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.solomonboltin.telegramtv3.connect.Connect
import com.solomonboltin.telegramtv3.utils.QrUtils
import com.solomonboltin.telegramtv3.vms.ClientVM
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.androidx.compose.koinViewModel

fun authToQr(authState: TdApi.AuthorizationState): Painter {
    return when (authState) {
        is TdApi.AuthorizationStateWaitOtherDeviceConfirmation -> {
            BitmapPainter(QrUtils.generateQrCode(authState.link)!!)
        }
        else -> {
            BitmapPainter(QrUtils.generateQrCode("Error")!!)
        }
    }
}

@Composable
fun ConnectionUI() {
    val clientVm: ClientVM = koinViewModel()
    val authState by clientVm.authState.collectAsState()

    when (authState) {
        is TdApi.AuthorizationStateWaitTdlibParameters -> {
            Text("Connecting to telegram")
        }
        is TdApi.AuthorizationStateWaitEncryptionKey -> {
            Text("Waiting for encryption key")
        }
        is TdApi.AuthorizationStateWaitPhoneNumber -> Text("Wait phone number")
        is TdApi.AuthorizationStateWaitOtherDeviceConfirmation -> {
            authState as TdApi.AuthorizationStateWaitOtherDeviceConfirmation

            Connect(
                Modifier
                    .fillMaxSize(),
                qrImage = authToQr(authState as TdApi.AuthorizationStateWaitOtherDeviceConfirmation)
            )
        }
        is TdApi.AuthorizationStateClosed -> {
            Text(text = "Connection closed")
        }
        else -> Text("Unknown Unauthorized state: $authState")
    }
}
