package com.example.getmyparking.utils.enums

import com.google.gson.annotations.SerializedName

enum class PaymentAt {
    @SerializedName("CHECK_IN")
    CHECK_IN,
    @SerializedName("CHECK_OUT")
    CHECK_OUT
}