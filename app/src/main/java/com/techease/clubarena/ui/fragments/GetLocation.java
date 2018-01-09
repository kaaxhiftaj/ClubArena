package com.techease.clubarena.ui.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.techease.clubarena.R;
import com.techease.clubarena.ui.activities.MainActivity;

import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class GetLocation extends Fragment {

    @BindView(R.id.use_my_location)
    Button btn_use_my_location ;

    Unbinder unbinder;
    double lat, lon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_get_location, container, false);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Raleway-ExtraBold.ttf");
     //   btn_use_my_location.setTypeface(custom_font);
        unbinder = ButterKnife.bind(this, v);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        SmartLocation.with(getActivity()).location()
                .start(new OnLocationUpdatedListener() {

                    @Override
                    public void onLocationUpdated(Location location) {
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                        Log.d("Location : ", "" + lat + " " + lon);
                        Toast.makeText(getActivity(), String.valueOf(lat), Toast.LENGTH_SHORT).show();

                            }


                });


        btn_use_my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon );
                startActivity(intent);
            }
        });
        return v;
    }

}
