package com.solomonboltin.telegramtv.ui.connection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.solomonboltin.telegramtv.vms.ClientVM
import com.solomonboltin.telegramtv.utils.QrUtils
import org.drinkless.td.libcore.telegram.TdApi
import org.koin.androidx.compose.koinViewModel

@Composable
fun QrScanUI(clientVM: ClientVM = koinViewModel()) {

    val state by clientVM.authState.collectAsState()

    Box(modifier = Modifier
        .height(400.dp)
        .width(400.dp)
        .background(color = Color.White) )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val qrCodeBitmap = QrUtils.generateQrCode((state as TdApi.AuthorizationStateWaitOtherDeviceConfirmation).link)
            Image(
                painter = BitmapPainter(qrCodeBitmap!!),
                contentDescription = "qr code",
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp),
                contentScale = ContentScale.FillWidth
            )
            Text(text = "Scan this QR code from your Telegram mobile app")
        }
    }
}


