package com.solomonboltin.telegramtv.ui.connection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Device
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.solomonboltin.telegramtv.utils.QrUtils
import com.solomonboltin.telegramtv.vms.ClientVM
import com.solomonboltin.telegramtv.connect.Connect
import com.solomonboltin.telegramtv.vms.AppVM
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
    val appVM: AppVM = koinViewModel()
    val clientVm: ClientVM = koinViewModel()
    val authState by clientVm.authState.collectAsState()

    when (authState) {
        is TdApi.AuthorizationStateWaitTdlibParameters -> {
            Text("Connecting to telegram")
        }

        is TdApi.AuthorizationStateWaitEncryptionKey -> {
            Text("Waiting for encryption key")
        }

        is TdApi.AuthorizationStateWaitPhoneNumber ->
            Text("Wait phone number")

        is TdApi.AuthorizationStateWaitOtherDeviceConfirmation -> {
            authState as TdApi.AuthorizationStateWaitOtherDeviceConfirmation

            val qrImage =  authToQr(authState as TdApi.AuthorizationStateWaitOtherDeviceConfirmation)
            if (appVM.isTv) {
                Connect(
                    Modifier
                        .fillMaxSize(),
                    qrImage = qrImage
                )
            } else {
                MobileConnectView(qrImage = qrImage)
            }

        }

        is TdApi.AuthorizationStateClosed -> {
            Text(text = "Connection closed")
        }

        else -> Text("Unknown Unauthorized state: $authState")
    }
}

@Preview(device = Devices.PIXEL_4)
@Composable
fun MobileConnectView(
    qrImage: Painter = BitmapPainter(QrUtils.generateQrCode("test")!!)
) {

    val telegramBlue = Color(0xFF0088CC)
    val telegramWhite = Color.White
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(telegramBlue)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
            ,
            horizontalAlignment = Alignment.CenterHorizontally,



        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(all = 16.dp)
                    .background(telegramWhite)
                    .fillMaxWidth()
                    .fillMaxHeight(0.7F)
                    // rounded corners
                    .clip(RoundedCornerShape(30.dp, 30.dp, 30.dp, 30.dp)) // Round top corners


                ,
            ) {
                Image(
                    painter = qrImage,
                    contentDescription = "QR code for telegram",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text(
                text = "scan this from your telegram app on mobile",
                color = telegramWhite,
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(top = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontWeight = FontWeight.Bold

            )

        }
    }
}
