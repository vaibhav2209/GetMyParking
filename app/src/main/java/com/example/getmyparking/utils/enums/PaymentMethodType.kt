package com.example.getmyparking.utils.enums

import com.google.gson.annotations.SerializedName

enum class PaymentMethodType {
    @SerializedName("CASH")
    CASH,
    @SerializedName("DEBIT_CARD")
    DEBIT_CARD,
    @SerializedName("CREDIT_CARD")
    CREDIT_CARD,
    @SerializedName("MOBILE_WALLETS")
    MOBILE_WALLETS,
    @SerializedName("ONLINE_PAYMENT")
    ONLINE_PAYMENT,
    @SerializedName("OTHER")
    OTHER
}