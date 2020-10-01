package com.example.eatathome.Client.Services;

import com.example.eatathome.Client.Constant.Constant;
import com.example.eatathome.Client.Model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        if (Constant.currentUser != null)
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
