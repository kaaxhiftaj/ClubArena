package com.techease.clubarena.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techease.clubarena.R;
import com.techease.clubarena.models.BlogModel;
import com.techease.clubarena.ui.adapters.BlogAdapter;
import com.techease.clubarena.utils.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BlogFragment extends Fragment {

    GridView gridView;
    ArrayList<BlogModel> blogModels;
    BlogAdapter blogAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blog, container, false);

        gridView=(GridView)v.findViewById(R.id.gridView);
        apicall();
        return v;
    }

    private void apicall() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.USER_URL+" "
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zma  reg response", response);
                //   DialogUtils.sweetAlertDialog.dismiss();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //DialogUtils.sweetAlertDialog.dismiss();
                // DialogUtils.showErrorTypeAlertDialog(getActivity(), "Server error");
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
