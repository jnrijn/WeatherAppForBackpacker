package com.example.weatherappforbackpacker.model.repository

import com.example.weatherappforbackpacker.model.LocationList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {
    @GET("api/location/search")
    fun getList(@Query("query") query: String): Observable<ArrayList<LocationList>>
}