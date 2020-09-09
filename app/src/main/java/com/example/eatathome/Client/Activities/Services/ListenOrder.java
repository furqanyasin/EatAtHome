package com.example.eatathome.Client.Activities.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.eatathome.Client.Activities.OrderStatusActivity;
import com.example.eatathome.Constant.Constant;
import com.example.eatathome.Models.Request;
import com.example.eatathome.R;
import com.example.eatathome.Server.Activities.OrderStatusActivityRes;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Key;
import java.util.Random;

public class ListenOrder extends Service implements ChildEventListener {

    FirebaseDatabase database;
    DatabaseReference requests;

    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Request");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requests.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        Request request = dataSnapshot.getValue(Request.class);
        if (request.getStatus().equals("0"))
            showNotification(dataSnapshot.getKey(),request);

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        Request request = dataSnapshot.getValue(Request.class);
        showNotification(dataSnapshot.getKey(), request);

    }

    private void showNotification(String key, Request request) {
        Intent intent = new Intent(getBaseContext(), OrderStatusActivityRes.class);
        intent.putExtra("userPhone", request.getPhone()); //we need put user phone
    /*    PendingIntent contentIntent = PendingIntent
                .getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);*/
        PendingIntent contentIntent = PendingIntent
                .getActivity(getBaseContext(), 0, intent,0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("foodStatus", "foodStatus", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "foodStatus");
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("food")
                .setContentInfo("your order was updated")
                .setContentText("Order # " + key + " was updated to " + Constant.convertCodeToStatus(request.getStatus()))
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher);


        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        int randomInt = new Random().nextInt(9999-1)+1;
        notificationManager.notify(randomInt, builder.build());

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
