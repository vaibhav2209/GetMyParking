package com.example.getmyparking.utils.enums

import com.google.gson.annotations.SerializedName

enum class TicketingSystem {
    @SerializedName("COMPUTER_SYSTEM")
    COMPUTER_SYSTEM,
    @SerializedName("HANDHELD_SYSTEM")
    HANDHELD_SYSTEM,
    @SerializedName("MANUAL_PRE_PRINTED")
    MANUAL_PRE_PRINTED,
    @SerializedName("NO_TICKETING_SYSTEM")
    NO_TICKETING_SYSTEM,
    @SerializedName("TICKET_DISPENSER")
    TICKET_DISPENSER
}