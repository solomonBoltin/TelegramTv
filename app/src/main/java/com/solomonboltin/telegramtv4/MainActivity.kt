package com.solomonboltin.telegramtv4

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.solomonboltin.telegramtv4.frame18.Frame18
import com.solomonboltin.telegramtv4.ui.MainUI
import com.solomonboltin.telegramtv4.vms.FilesVM
import com.solomonboltin.telegramtv4.vms.MovieDashVM
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel

class MainActivity : AppCompatActivity() {
    private val dashVm : MovieDashVM by inject()
    private val filesVM : FilesVM by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity", "Main activity created")
        supportActionBar?.hide();
        setContent{   MainUI()   }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.i("MainActivity", "Key pressed: $keyCode")
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            dashVm.navigatePrev()
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            dashVm.navigateNext()
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            dashVm.navigatePrevList()
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            dashVm.navigateNextList()
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            val movie = dashVm.dashData.value.selectedMovie
            if (movie != null) filesVM.setMovie(movie.toMovie())
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}