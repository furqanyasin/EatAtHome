package com.example.eatathome.Remote;


import com.example.eatathome.Server.Models.MyResponseRes;
import com.example.eatathome.Server.Models.SenderRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIServiceRes {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA5aoviMg:APA91bFp-sqQDcoAyWazSzWkkazTrGvQhZTyTwqFmwi2PYyopCk5Zv4Pqf71b4pmpSAKDCrQufwv3XrqIUvKMzsMX0v4E7kPiFr9H4i8EdRrlb3TB2FL6O4VHINWcbc5mExgQ7r0q3La"
            }

    )

    @POST("fcm/send")
    Call<MyResponseRes> sendNotification(@Body SenderRes body);
}
