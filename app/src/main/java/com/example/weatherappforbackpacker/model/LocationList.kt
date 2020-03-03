package com.example.weatherappforbackpacker.model

import com.google.gson.annotations.SerializedName

data class LocationList(
    @SerializedName("woeid") var woeid: Int
)