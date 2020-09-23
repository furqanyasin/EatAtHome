package com.example.eatathome.Server.Activities.Service;

import com.example.eatathome.Client.Activities.Constant.Constant;
import com.example.eatathome.Client.Activities.Model.Token;
import com.example.eatathome.Server.Activities.Constant.ConstantRes;
import com.example.eatathome.Server.Activities.Models.TokenRes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdServiceRes extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        if (ConstantRes.currentUser != null)
        updateTokenToFirebase(tokenRefreshed);
    }

    private void updateTokenToFirebase(String tokenRefreshed) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        TokenRes token = new TokenRes(tokenRefreshed, true);
        // false because token send from client app

        tokens.child(Constant.currentUser.getPhone()).setValue(token);
    }
}
