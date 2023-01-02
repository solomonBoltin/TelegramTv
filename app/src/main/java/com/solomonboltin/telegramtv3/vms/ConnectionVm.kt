package com.solomonboltin.telegramtv3.vms

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.solomonboltin.telegramtv3.models.ClientStatus

class ConnectionVm : ViewModel() {
    val clientStatus = mutableStateOf<ClientStatus>(ClientStatus.Unknown)
    fun updateClientStatus(clientStatus: ClientStatus) {
        this.clientStatus.value = clientStatus
    }


}
