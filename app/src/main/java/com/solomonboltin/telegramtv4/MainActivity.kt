package com.solomonboltin.telegramtv4

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.solomonboltin.telegramtv4.ui.MainUI

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity", "Main activity created")
        supportActionBar?.hide();
        setContent{ MainUI() }
    }
}