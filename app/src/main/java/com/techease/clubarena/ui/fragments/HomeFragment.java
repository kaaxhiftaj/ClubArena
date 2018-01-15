package com.techease.clubarena.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techease.clubarena.R;
import com.techease.clubarena.models.HomeModel;
import com.techease.clubarena.ui.adapters.HomeAdapter;
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
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class HomeFragment extends Fragment {

    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String user_id ;
    List<HomeModel> home_model_list;
    HomeAdapter home_adapter;
    double lat, lon ;
    Unbinder unbinder;

    @BindView(R.id.rv_home)
    RecyclerView recyclerView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, v);

        if(InternetUtils.isNetworkConnected(getActivity()))
        {

            sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            user_id = sharedPreferences.getString("user_id","");


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
//            SmartLocation.with(getActivity()).location()
//                    .start(new OnLocationUpdatedListener() {
//
//                        @Override
//                        public void onLocationUpdated(Location location) {
//                            lat = location.getLatitude();
//                            lon = location.getLongitude();
//
//                        }
//                    });


            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            home_model_list = new ArrayList<>();
            apicall();
            if (alertDialog == null)
                alertDialog = AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();
            home_adapter = new HomeAdapter(getActivity(), home_model_list);
            recyclerView.setAdapter(home_adapter);

        }
        else
        {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        return v;
    }



    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://barapp.adadigbomma.com/App/getAllBars"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                try {
                    if (alertDialog != null)
                        alertDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArr = jsonObject.getJSONArray("shops");
                    for (int i=0; i<jsonArr.length(); i++)
                    {
                        JSONObject temp = jsonArr.getJSONObject(i);

                        HomeModel model=new HomeModel();
                        String club_name = temp.getString("bar_name");
                        String club_id = temp.getString("bar_id");
                        String picture =temp.getString("picture");
                        String distance =temp.getString("distance");
                        String longitude =temp.isNull("longitude")?null: temp.getString("longitude");
                        String latitude =temp.isNull("latitude")?null: temp.getString("latitude");
                        String isFavorite =temp.getString("IsFavorite");
                        String open_time =temp.getString("open_time");
                        String close_time =temp.getString("close_time");
                        String totalReviews =temp.getString("totalReviews");
                        String rating =temp.getString("rating");

                        model.setClub_name(club_name);
                        model.setId(club_id);
                        model.setImage_url(picture);
                        model.setLat(latitude);
                        model.setLng(longitude);
                        model.setRating(rating);

                        home_model_list.add(model);

                    }
                    home_adapter.notifyDataSetChanged();

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
                params.put("latitude", String.valueOf(lat));
                params.put("longitude", String.valueOf(lon));
                params.put("user_id", user_id);
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }
}
