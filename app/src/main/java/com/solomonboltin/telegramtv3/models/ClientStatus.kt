package com.solomonboltin.telegramtv3.models

sealed interface ClientStatus {
    object Unknown : ClientStatus
    data class WaitingForQrCode(val linkString: String) : ClientStatus
    data class Connected(val userDetails: String) : ClientStatus

}
