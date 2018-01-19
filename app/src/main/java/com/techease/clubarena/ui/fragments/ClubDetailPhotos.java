package com.techease.clubarena.ui.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
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
import com.techease.clubarena.models.ClubDetailsPhotoModel;
import com.techease.clubarena.models.ModelClubDetails;
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

public class ClubDetailPhotos extends Fragment {

    List<ClubDetailsPhotoModel> images_model_list;
    ClubDetailsPhotoAdapter images_adapter;
    Unbinder unbinder;

    @BindView(R.id.club_detail_photo)
    GridView gridView ;
    String club_id ;
    android.support.v7.app.AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_club_detail_photos, container, false);
        unbinder = ButterKnife.bind(this, v);
        club_id =getArguments().getString("club_id");

        if(InternetUtils.isNetworkConnected(getActivity()))
        {

            club_id =getArguments().getString("club_id");
            images_model_list = new ArrayList<>();
            gridView.setAdapter(images_adapter);
            apicall();
//            if (alertDialog == null)
//                alertDialog = AlertsUtils.createProgressDialog(getActivity());
//            alertDialog.show();

        }
        else
        {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }
        return  v ;
    }



    private void apicall() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL+"App/clubimages"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray jsonArr=jsonObject.getJSONArray("images");
                        images_model_list = new ArrayList<>();

                        for (int i=0; i<jsonArr.length(); i++)
                        {
                            JSONObject temp = jsonArr.getJSONObject(i);
                            ClubDetailsPhotoModel photoModel=new ClubDetailsPhotoModel();

                            photoModel.setId(temp.getString("id"));
                            photoModel.setImage_url(temp.getString("image"));
                            images_model_list.add(photoModel);

                        }
                        if (getActivity()!=null)
                        {
                            images_adapter=new ClubDetailsPhotoAdapter(getActivity(),images_model_list);
                            gridView.setAdapter(images_adapter);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                } else {
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
                params.put("club_id" , club_id);
                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }



}
