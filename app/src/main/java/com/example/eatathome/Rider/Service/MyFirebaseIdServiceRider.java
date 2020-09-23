package com.example.eatathome.Rider.Service;

import com.example.eatathome.Client.Activities.Model.Token;
import com.example.eatathome.Rider.Constant.ConstantRider;
import com.example.eatathome.Rider.Model.TokenRider;
import com.example.eatathome.Server.Activities.Constant.ConstantRes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdServiceRider extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        if (ConstantRider.currentRider != null)
            updateTokenToFirebase(tokenRefreshed);
    }

    private void updateTokenToFirebase(String tokenRefreshed) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        TokenRider token = new TokenRider(tokenRefreshed, false);
        // false because token send from client app

        tokens.child(ConstantRider.currentRider.getPhone()).setValue(token);
    }
}
