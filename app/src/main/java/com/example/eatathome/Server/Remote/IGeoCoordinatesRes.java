package com.example.eatathome.Server.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGeoCoordinatesRes {
    @GET("maps/api/geocode/json?key=AIzaSyDF92UBWwV_F9EBtozTSZQReIPI3v0sOcw")
    Call<String> getGeoCode(@Query("address") String address);

    @GET("maps/api/directions/json?key=AIzaSyDF92UBWwV_F9EBtozTSZQReIPI3v0sOcw")
    Call<String> getDirections(@Query("origin") String origin, @Query("destination") String destination);


}
