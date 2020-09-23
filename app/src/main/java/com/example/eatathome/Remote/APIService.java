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
                    "Authorization:key=AAAA5aoviMg:APA91bFp-sqQDcoAyWazSzWkkazTrGvQhZTyTwqFmwi2PYyopCk5Zv4Pqf71b4pmpSAKDCrQufwv3XrqIUvKMzsMX0v4E7kPiFr9H4i8EdRrlb3TB2FL6O4VHINWcbc5mExgQ7r0q3La"
            }

    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
