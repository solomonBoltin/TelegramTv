package com.solomonboltin.telegramtv3

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.solomonboltin.telegramtv3.ui.MainView
import com.solomonboltin.telegramtv3.vms.AppVm

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity", "hello world")

        setContent{
            MainView(AppVm(this))
        }
    }
}