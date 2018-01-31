package com.techease.clubarena.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techease.clubarena.R;
import com.techease.clubarena.models.ModelClubDetails;
import com.techease.clubarena.ui.activities.SplashScreen;
import com.techease.clubarena.ui.adapters.ClubDetailsPhotoAdapter;
import com.techease.clubarena.utils.AlertsUtils;
import com.techease.clubarena.utils.Configuration;
import com.techease.clubarena.utils.InternetUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;



public class ClubDetailsInfo extends Fragment {

    @BindView(R.id.mapView)
    MapView mMapView;

    @BindView(R.id.tv_club_name)
    TextView tv_club_name;

    @BindView(R.id.tv_club_type)
            TextView tv_club_type ;

    @BindView(R.id.tv_open_time)
            TextView tv_open_time ;

    @BindView(R.id.tv_close_time)
            TextView tv_close_time ;

    @BindView(R.id.tv_club_info)
            TextView tv_club_info ;

    Unbinder unbinder;

    String club_id ;

    String name , club_type, open_time , close_time, information, latitude, longitude  ;
    Typeface custom_font ;
    private GoogleMap googleMap;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    android.support.v7.app.AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_club_details_info, container, false);
        unbinder = ButterKnife.bind(this,v );
        club_id =getArguments().getString("club_id");

        custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-ExtraBold.ttf");
        tv_club_name.setTypeface(custom_font);
        tv_club_type.setTypeface(custom_font);
        tv_open_time.setTypeface(custom_font);
        tv_close_time.setTypeface(custom_font);
        tv_club_info.setTypeface(custom_font);

        if(InternetUtils.isNetworkConnected(getActivity()))
        {
            apicall();
            if (alertDialog == null)
                alertDialog = AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume();


            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;

                    // For showing a move to my location button
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    googleMap.setMyLocationEnabled(true);

                   LatLng sydney = new LatLng( 53.558, 9.927 );
                    googleMap.addMarker(new MarkerOptions().position(sydney).title(name));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });


        }
        else
        {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }





    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL+"App/clubdetail"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    try {
                        if (alertDialog != null)
                            alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject temp = jsonObject.getJSONObject("bar");
                         name = temp.getString("name");
                        club_type = temp.getString("club_type");
                        information = temp.getString("information");
                        open_time = temp.getString("open_time");
                        close_time = temp.getString("close_time");
                        latitude = temp.getString("latitude");
                        longitude = temp.getString("longitude");

                        tv_club_name.setText(name);
                        tv_club_type.setText(club_type);
                        tv_open_time.setText("Open Time : " + open_time);
                        tv_close_time.setText("Close Time : " + close_time);
                        tv_club_info.setText("About Club : " + information);
                        float lat = Float.valueOf(latitude);
                        float lon = Float.valueOf(longitude);
                        putMarker( lat, lon);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (alertDialog != null)
                            alertDialog.dismiss();
                    }
                }
                else {
                    try {
                        if (alertDialog != null)
                            alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        AlertsUtils.showErrorDialog(getActivity(), message);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (alertDialog != null)
                            alertDialog.dismiss();
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog != null)
                    alertDialog.dismiss();
                Log.d("error" , String.valueOf(error.getCause()));

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("club_id", club_id);

                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }



    public  void  putMarker(float lat, float lon){
        LatLng sydney = new LatLng( lat, lon  );
        googleMap.addMarker(new MarkerOptions().position(sydney).title(""));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

}
