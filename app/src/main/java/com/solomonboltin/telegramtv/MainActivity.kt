package com.solomonboltin.telegramtv

import android.content.pm.ConfigurationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.solomonboltin.telegramtv.ui.MainMobileUI
import com.solomonboltin.telegramtv.ui.MainTVUI
import com.solomonboltin.telegramtv.vms.AppVM
import com.solomonboltin.telegramtv.vms.FilesVM
import com.solomonboltin.telegramtv.vms.MovieDashVM
import com.solomonboltin.telegramtv.vms.PlayerVM
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {
    private val appVM: AppVM by inject()
    private val dashVm: MovieDashVM by inject()
    private val playerVM: PlayerVM by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity", "Main activity created")
        supportActionBar?.hide();

        if (appVM.isTv) {
            Log.i("MainActivity", "App is running on a TV")
            // set app to landscape mode
            requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        } else {
            Log.i("MainActivity", "App is running on a non-TV")
            // set app to portrait mode
            requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        setContent { MainTVUI() }


    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        Log.i("MainActivity", "Key pressed: $keyCode")
//        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//            dashVm.navigatePrev()
//            return true
//        }
//        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//            dashVm.navigateNext()
//            return true
//        }
//        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
//            dashVm.navigatePrevList()
//            return true
//        }
//        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
//            dashVm.navigateNextList()
//            return true
//        }
//        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
//            val movie = dashVm.dashData.value.selectedMovie
//            if (movie != null) playerVM.setMovie(movie.toMovieDa())
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }

}