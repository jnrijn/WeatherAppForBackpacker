package com.example.weatherappforbackpacker.model

import com.google.gson.annotations.SerializedName

data class WeatherList(
    @SerializedName("consolidated_weather") var weathers: ArrayList<Items>,
    @SerializedName("title") var title: String,
    @SerializedName("woeid") var woeid: Int
) {
    data class Items(
        @SerializedName("weather_state_name") var weather: String,
        @SerializedName("weather_state_abbr") var weatherIconName: String,
        @SerializedName("humidity") var humidity: Int,
        @SerializedName("the_temp") var temp: Double,
        @SerializedName("applicable_date") var date: String
    )
}