package com.solomonboltin.telegramtv3.ui.connection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.solomonboltin.telegramtv3.utils.QrUtils
import com.solomonboltin.telegramtv3.vms.AppVm
import org.drinkless.td.libcore.telegram.TdApi

@Composable
fun RequestQrScan(viewModel: AppVm) {

    val state by viewModel.authState.collectAsState()

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
                painter = BitmapPainter(qrCodeBitmap!!.asImageBitmap()),
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


