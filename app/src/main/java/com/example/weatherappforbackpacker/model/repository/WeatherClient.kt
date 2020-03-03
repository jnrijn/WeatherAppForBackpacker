package com.example.weatherappforbackpacker.model.repository

import com.example.weatherappforbackpacker.constant.CommonConstant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherClient {
    fun getWeather(): WeatherApi = Retrofit.Builder()
        .baseUrl(CommonConstant.BASE_URL)
        .client(OkHttpClient())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)
}