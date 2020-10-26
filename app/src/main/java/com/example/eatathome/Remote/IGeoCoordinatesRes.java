package com.example.eatathome.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGeoCoordinatesRes {
    @GET("maps/api/geocode/json?key=")
    Call<String> getGeoCode(@Query("address") String address);

    @GET("maps/api/directions/json?key=")
    Call<String> getDirections(@Query("origin") String origin, @Query("destination") String destination);


}
