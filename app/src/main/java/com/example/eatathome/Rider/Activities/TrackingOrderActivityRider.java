package com.example.eatathome.Rider.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatathome.R;
import com.example.eatathome.Remote.IGeoCoordinatesRes;
import com.example.eatathome.Rider.Constant.ConstantRider;
import com.example.eatathome.Rider.Constant.DirectionJSONParserRider;
import com.example.eatathome.Rider.Model.RequestRider;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingOrderActivityRider extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    Location mLastLocation;

    Marker mCurrentMarker;
    IGeoCoordinatesRes mService;
    Polyline polyline;

    MaterialButton btn_shipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_order_rider);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_shipped = findViewById(R.id.btnShipped);
        btn_shipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfirmDialog();

            }
        });


        mService = ConstantRider.getGeoCodeService();

        buildLocationRequest();
        buildLocationCallBack();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }


    private void ConfirmDialog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TrackingOrderActivityRider.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialog.setTitle("Confirm Shipped?");

        LayoutInflater inflater = this.getLayoutInflater();
        View confirm_delete_layout = inflater.inflate(R.layout.confirm_layout, null);
        alertDialog.setView(confirm_delete_layout);
        alertDialog.setIcon(R.drawable.ic_local_shipping_black_24dp);

        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //we will delete order in the table
                //OrderNeedShip or ShippingOrder
                //And udate status of order to Shipped
                FirebaseDatabase.getInstance()
                        .getReference(ConstantRider.ORDER_NEED_SHIP_TABLE)
                        .child(ConstantRider.currentRider.getPhone())
                        .child(ConstantRider.currentKey)
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Update status on Request table
                                Map<String, Object> update_status = new HashMap<>();
                                update_status.put("status", "03");

                                FirebaseDatabase.getInstance()
                                        .getReference("Requests")
                                        .child(ConstantRider.currentKey)
                                        .updateChildren(update_status)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Delete from shippingOrder
                                                FirebaseDatabase.getInstance()
                                                        .getReference(ConstantRider.SHIPPER_INFO_TABLE)
                                                        .child(ConstantRider.currentKey)
                                                        .removeValue()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(TrackingOrderActivityRider.this, "Delivered!", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        });
                                            }
                                        });
                            }
                        });
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }


    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                mLastLocation = locationResult.getLastLocation();
                if (mCurrentMarker != null)
                    mCurrentMarker.setPosition(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())); //update location for marker

                //update location on firebase
                ConstantRider.updateShippingInformation(ConstantRider.currentKey, mLastLocation);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(),
                        mLastLocation.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                drawRoute(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), ConstantRider.currentRequest);

            }
        };
    }

    private void drawRoute(final LatLng yourLocation, final RequestRider request) {

        //clear all polyline
        if (polyline != null)
            polyline.remove();

        if (request.getAddress() != null && !request.getAddress().isEmpty()) {
            mService.getGeoCode(request.getAddress()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        String lat = ((JSONArray) jsonObject.get("results"))
                                .getJSONObject(0)
                                .getJSONObject("geometry")
                                .getJSONObject("location")
                                .get("lat").toString();

                        String lng = ((JSONArray) jsonObject.get("results"))
                                .getJSONObject(0)
                                .getJSONObject("geometry")
                                .getJSONObject("location")
                                .get("lng").toString();

                        final LatLng orderLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.box);
                        bitmap = ConstantRider.scaleBitmap(bitmap, 70, 70);

                        MarkerOptions marker = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                .title("Order of " + ConstantRider.currentRequest.getPhone())
                                .position(orderLocation);

                        mMap.addMarker(marker);

                        //draw route

                        mService.getDirections(yourLocation.latitude + "," + yourLocation.longitude,
                                orderLocation.latitude + "," + orderLocation.longitude)
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {

                                        new ParserTask().execute(response.body());

                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {

                                    }
                                });

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        } else {
            if (request.getLatLng() != null && !request.getLatLng().isEmpty()) {
                String[] latLng = request.getLatLng().split(",");
                LatLng orderLocation = new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.box);
                bitmap = ConstantRider.scaleBitmap(bitmap, 70, 70);

                MarkerOptions marker = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                        .title("Order of " + ConstantRider.currentRequest.getPhone())
                        .position(orderLocation);

                mMap.addMarker(marker);
                mService.getDirections(mLastLocation.getLatitude() + "," + mLastLocation.getLongitude(),
                        orderLocation.latitude + "," + orderLocation.longitude)
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                new ParserTask().execute(response.body().toString());

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
            }
        }
    }


    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(10f);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
    }

    @Override
    protected void onStop() {
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker and move the camera
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mLastLocation = location;
                final LatLng yourLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mCurrentMarker = mMap.addMarker(new MarkerOptions().position(yourLocation).title("Your location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(yourLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        showGoogleMapDirection(new LatLng(mLastLocation.getLatitude(),
                                mLastLocation.getLongitude()), ConstantRider.currentRequest);
                        return false;
                    }
                });
            }
        });
    }

    private void showGoogleMapDirection(final LatLng yourLocation, final RequestRider request) {
        if (request.getAddress() != null && !request.getAddress().isEmpty()) {
            mService.getGeoCode(request.getAddress()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        String lat = ((JSONArray) jsonObject.get("results"))
                                .getJSONObject(0)
                                .getJSONObject("geometry")
                                .getJSONObject("location")
                                .get("lat").toString();

                        String lng = ((JSONArray) jsonObject.get("results"))
                                .getJSONObject(0)
                                .getJSONObject("geometry")
                                .getJSONObject("location")
                                .get("lng").toString();

                        final LatLng orderLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                        String uri = "http://maps.google.com/maps?saddr=" + yourLocation.latitude + "," + yourLocation.longitude + "&daddr=" + orderLocation.latitude + "," + orderLocation.longitude;
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);


                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        } else {
            if (request.getLatLng() != null && !request.getLatLng().isEmpty()) {
                String[] latLng = request.getLatLng().split(",");
                LatLng orderLocation = new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));

                String uri = "http://maps.google.com/maps?saddr=" + yourLocation.latitude + "," + yourLocation.longitude + "&daddr=" + orderLocation.latitude + "," + orderLocation.longitude;
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        android.app.AlertDialog mDialog = new SpotsDialog(TrackingOrderActivityRider.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.setMessage("Please waiting...");
            mDialog.show();

        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {

            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jsonObject = new JSONObject(strings[0]);

                DirectionJSONParserRider parser = new DirectionJSONParserRider();
                routes = parser.parse(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            mDialog.dismiss();

            ArrayList points = null;
            PolylineOptions lineOptions = null;

            for (int i = 0; i < lists.size(); i++) {

                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = lists.get(i);

                for (int j = 0; j < path.size(); j++) {

                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));

                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);
            }

            mMap.addPolyline(lineOptions);
        }
    }
}