package com.solomonboltin.telegramtv3

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.solomonboltin.telegramtv3.models.ClientStatus
import com.solomonboltin.telegramtv3.ui.MainUI
import com.solomonboltin.telegramtv3.vms.AppVm
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val clientStatus: ClientStatus by inject()
    private val appVm by viewModel<AppVm>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MyLog", "hello world $clientStatus")
        Log.i("MyLog", "hello world ${appVm.authState}")


        setContent{
            MainUI()
        }
    }
}