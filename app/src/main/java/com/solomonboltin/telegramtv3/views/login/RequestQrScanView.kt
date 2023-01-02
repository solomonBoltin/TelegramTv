package com.solomonboltin.telegramtv3.views.login

// compose view in size of 2/3 of screen named requestQrScan
// in view there should be an image view called qrCodeImage and under it text that tells user to scan qr code from his telegram mobile app
// view should get a link string and use generateQrCode function to generate qr code bitmap, then apply it
// the link string should be reactive and connectect to view model
// the view should be reactive to view model and update qr code image when link string changes


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.solomonboltin.telegramtv3.utils.QrUtils.generateQrCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.td.libcore.telegram.TdApi


// requestQrScanViewModel with link string
// requestQrScanView with qr code image
// requestQrScanView should be reactive to requestQrScanViewModel and update qr code image when link string changes

class RequestQrScanViewModel() : ViewModel() {

    val _link = MutableStateFlow<String?>(null)
    val link: StateFlow<String?> = _link.asStateFlow()

    fun setLink(link: String) {
        _link.update {
            link
        }
    }
}


@Composable
fun RequestQrScan(viewModel: TgView) {

    val state by viewModel.authState.collectAsState()

    Box(modifier = Modifier
        .height(400.dp)
        .width(400.dp)
        .background(color = androidx.compose.ui.graphics.Color.White) )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val qrCodeBitmap = generateQrCode((state as TdApi.AuthorizationStateWaitOtherDeviceConfirmation).link)
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


