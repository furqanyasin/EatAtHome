package com.example.eatathome.Client.Activities.Services;

import com.example.eatathome.Client.Activities.Constant.Constant;
import com.example.eatathome.Client.Activities.Model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s){
        super.onNewToken(s);
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        if(Constant.currentUser != null)
            updateTokenToFirebase(tokenRefreshed);
    }

    private void updateTokenToFirebase(String tokenRefreshed){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token token = new Token(tokenRefreshed, false);
        // false because token send from client app

        tokens.child(Constant.currentUser.getPhone()).setValue(token);
    }
}
