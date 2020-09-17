package com.example.eatathome.Remote;

import com.example.eatathome.Client.Activities.Model.MyResponse;
import com.example.eatathome.Client.Activities.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AIzaSyBanwRKl5Nsls3axT7N5x5M-DpV6TjAV0k"
            }

    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
