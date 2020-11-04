package com.example.eatathome.Rider.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGeoCoordinatesRider {
    @GET("maps/api/geocode/json?key=AIzaSyDCw5bW-WgoqDqYjfRd_K832NS4OYsl0zA&sensor=true&language=en")
    Call<String> getGeoCode(@Query("address") String address);

    @GET("maps/api/directions/json?key=AIzaSyDCw5bW-WgoqDqYjfRd_K832NS4OYsl0zA&sensor=true&language=en&mode=driving")
    Call<String> getDirections(@Query("origin") String origin, @Query("destination") String destination);
}
