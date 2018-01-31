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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.techease.clubarena.models.EventModel;
import com.techease.clubarena.models.HomeModel;
import com.techease.clubarena.ui.adapters.EventAdapter;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;


public class EventFragment extends Fragment {

    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String user_id ;
    List<EventModel> event_model_list;
    EventAdapter event_adapter;
    double lat, lon ;
    Unbinder unbinder;

    @BindView(R.id.rv_event)
    RecyclerView recyclerView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_event, container, false);
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
            SmartLocation.with(getActivity()).location()
                    .start(new OnLocationUpdatedListener() {

                        @Override
                        public void onLocationUpdated(Location location) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();

                        }
                    });


            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            event_model_list = new ArrayList<>();
            apicall();
            if (alertDialog == null)
                alertDialog = AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();
            event_adapter = new EventAdapter(getActivity(), event_model_list);
            recyclerView.setAdapter(event_adapter);

        }
        else
        {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }
        return v ;
    }




    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL+"App/events"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    try {
                        if (alertDialog != null)
                            alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArr = jsonObject.getJSONArray("events");
                        for (int i=0; i<jsonArr.length(); i++)
                        {
                            JSONObject temp = jsonArr.getJSONObject(i);

                            EventModel model=new EventModel();
                            String club_name = temp.getString("bar_name");
                            String event_id = temp.getString("event_id");
                            String event_name =temp.getString("event_name");
                            String picture =temp.getString("picture");
                            String distance =temp.getString("distance");

                            model.setClub_name(club_name);
                            model.setId(event_id);
                            model.setImage_url(picture);
                            model.setEvent_name(event_name);
                            event_model_list.add(model);

                        }
                        event_adapter.notifyDataSetChanged();

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

