package com.example.weatherappforbackpacker.model

class Contents(weather: String, weatherImgString: String, var temperature: Int, var wetness: Int) {
    var weatherImgString: String? = weatherImgString
    var weather: String? = weather
}