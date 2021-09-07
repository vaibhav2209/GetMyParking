package com.example.getmyparking.utils.enums

import com.google.gson.annotations.SerializedName

enum class ParkingCategory {
    @SerializedName("HOSPITAL")
    HOSPITAL,
    @SerializedName("MALL")
    MALL,
    @SerializedName("SHOPPING_COMPLEX")
    SHOPPING_COMPLEX,
    @SerializedName("CORPORATE_OFFICE")
    CORPORATE_OFFICE,
    @SerializedName("MARKET")
    MARKET,
    @SerializedName("TRAIN_STATION")
    TRAIN_STATION,
    @SerializedName("BUS_STATION")
    BUS_STATION,
    @SerializedName("RELIGIOUS_INSTITUTE")
    RELIGIOUS_INSTITUTE,
    @SerializedName("MOVIE_THEATRE")
    MOVIE_THEATRE,
    @SerializedName("EVENT_VENUE")
    EVENT_VENUE,
    @SerializedName("MISCELLANEOUS")
    MISCELLANEOUS,
    @SerializedName("HOTEL")
    HOTEL,
    @SerializedName("EDUCATIONAL_INSTITUTE")
    EDUCATIONAL_INSTITUTE,
    @SerializedName("RESIDENTIAL")
    RESIDENTIAL
}