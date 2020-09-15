package com.example.eatathome.Server.Activities.Service;

import com.example.eatathome.Client.Activities.Constant.Constant;
import com.example.eatathome.Client.Activities.Model.Token;
import com.example.eatathome.Server.Activities.Constant.ConstantRes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdServiceRes extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        if (ConstantRes.currentUser != null)
            updateTokenToFirebase(tokenRefreshed);
    }

    private void updateTokenToFirebase(String tokenRefreshed) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token token = new Token(tokenRefreshed, false);
        // false because token send from client app

        tokens.child(Constant.currentUser.getPhone()).setValue(token);
    }
}
