package com.example.eatathome.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGeoCoordinatesRes {
    @GET("maps/api/geocode/json?key=AIzaSyBanwRKl5Nsls3axT7N5x5M-DpV6TjAV0k")
    Call<String> getGeoCode(@Query("address") String address);

    @GET("maps/api/directions/json?key=AIzaSyBanwRKl5Nsls3axT7N5x5M-DpV6TjAV0k")
    Call<String> getDirections(@Query("origin") String origin, @Query("destination") String destination);


}
