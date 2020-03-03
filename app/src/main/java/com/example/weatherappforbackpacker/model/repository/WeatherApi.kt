package com.example.weatherappforbackpacker.model.repository

import com.example.weatherappforbackpacker.model.WeatherList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApi {
    @GET("api/location/{woeid}")
    fun getList(@Path("woeid") woeid: Int): Observable<WeatherList>
}