package com.techease.clubarena.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Binder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.techease.clubarena.models.ClubDetailsPhotoModel;
import com.techease.clubarena.models.ClubDetailsVideoModel;
import com.techease.clubarena.ui.adapters.ClubDetailsPhotoAdapter;
import com.techease.clubarena.ui.adapters.ClubDetailsVideoAdapter;
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

public class ClubDetailVideos extends Fragment {

    RecyclerView rv_video ;
    Binder unbinder ;
    List<ClubDetailsVideoModel> video_list;
    ClubDetailsVideoAdapter videoAdapter;
    String club_id ;
    android.support.v7.app.AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_club_detail_videos, container, false);
       rv_video = (RecyclerView)v.findViewById(R.id.video_recycler);
        club_id =getArguments().getString("club_id");

        if(InternetUtils.isNetworkConnected(getActivity()))
        {

            rv_video.setLayoutManager(new LinearLayoutManager(getActivity()));
            video_list = new ArrayList<>();
            apicall();
//            if (alertDialog == null)
//                alertDialog = AlertsUtils.createProgressDialog(getActivity());
//            alertDialog.show();
            videoAdapter = new ClubDetailsVideoAdapter(getActivity(), video_list);
            rv_video.setAdapter(videoAdapter);

        }
        else
        {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        return  v ;
    }



    private void apicall() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL+"App/clubvideos"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray jsonArr=jsonObject.getJSONArray("videos");
                        for (int i=0; i<jsonArr.length(); i++)
                        {
                            JSONObject temp = jsonArr.getJSONObject(i);
                            ClubDetailsVideoModel videoModel=new ClubDetailsVideoModel();

                            videoModel.setId(temp.getString("id"));
                            videoModel.setTitle(temp.getString("title"));
                            video_list.add(videoModel);

                        }
                        videoAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (alertDialog != null)
                            alertDialog.dismiss();

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
