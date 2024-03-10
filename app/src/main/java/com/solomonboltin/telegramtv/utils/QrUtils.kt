package com.solomonboltin.telegramtv.utils

// function for generating qrcode bitmap from string

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import java.util.*

object QrUtils {
    private const val WHITE = -0x1
    private const val BLACK = -0x1000000
    private const val WIDTH = 1000
    private const val HEIGHT = 1000

    // fun that takes in x,y, mx, my, and returns colorfull colors based on x,y scales between green red and blue in RGB format
    private fun getPixelColor(x: Int, y: Int, mx: Int, my: Int): Int {

        // telegram dark blue color
        val telegramDarkBlue = Color.rgb(0, 136, 204)
        // telegram light blue color
        val telegramLightBlue = Color.rgb(0, 168, 255)
        val sp1 = 1
        val sp2 = 136
        val sp3 = 204

        // random int between 100-200
//        val sp1 = (150..255).random()
//        val sp2 = (150..255).random()
//        val sp3 = (150..255).random()


        val r = (sp1 * x / mx).toInt()
        val g = (sp2 * y / my).toInt()
        val b = (sp3 * (mx - x) / mx).toInt()
        // mix r,g,b randomly

        return Color.rgb(r,g,b)
    }


    @JvmStatic
    fun generateQrCode(data: String): ImageBitmap? {
        val hints: MutableMap<EncodeHintType, Any> = EnumMap(EncodeHintType::class.java)

        return try {
            val bitMatrix = MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (bitMatrix[x, y]) getPixelColor(x,y, width, height) else WHITE
                }
            }
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            // to ImageBitmap

            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            bitmap.asImageBitmap()
        } catch (e: WriterException) {
//            Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888).asImageBitmap()
            null
        }
    }


}