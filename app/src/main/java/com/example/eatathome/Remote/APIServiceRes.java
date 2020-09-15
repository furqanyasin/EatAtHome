package com.example.eatathome.Remote;


import com.example.eatathome.Client.Activities.Model.MyResponse;
import com.example.eatathome.Client.Activities.Model.Sender;
import com.example.eatathome.Server.Activities.Models.MyResponseRes;
import com.example.eatathome.Server.Activities.Models.SenderRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIServiceRes {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AIzaSyBanwRKl5Nsls3axT7N5x5M-DpV6TjAV0k"
            }

    )

    @POST("fcm/send")
    Call<MyResponseRes> sendNotification(@Body SenderRes body);
}
