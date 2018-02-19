package com.techease.clubarena.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
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
import com.techease.clubarena.utils.AlertsUtils;
import com.techease.clubarena.utils.Configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.Unbinder;
import io.reactivex.observers.TestObserver;


public class SocialLinks extends Fragment {

    ImageButton Fb,Tw,instagram,Mail;
    String getId,Face,Tweet,insta, web;
    WebView webiew;
    String club_id ;
    Unbinder unbinder;
    android.support.v7.app.AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_social_links, container, false);

        club_id =getArguments().getString("club_id");

        apicall();

        Toast.makeText(getActivity(), club_id, Toast.LENGTH_SHORT).show();
        Fb=(ImageButton)v.findViewById(R.id.btnfb);
        Tw=(ImageButton)v.findViewById(R.id.btnTw);
        instagram =(ImageButton)v.findViewById(R.id.btnInsta);
        Mail=(ImageButton)v.findViewById(R.id.btnMail);
        webiew=(WebView)v.findViewById(R.id.wv);


        Fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webiew.getSettings().setJavaScriptEnabled(true);
                webiew.getSettings().setPluginState(WebSettings.PluginState.ON);
                webiew.loadUrl(Face);
                webiew.setWebChromeClient(new WebChromeClient());
            }
        });
        Tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webiew.getSettings().setJavaScriptEnabled(true);
                webiew.getSettings().setPluginState(WebSettings.PluginState.ON);
                webiew.loadUrl(Tweet);
                webiew.setWebChromeClient(new WebChromeClient());
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webiew.getSettings().setJavaScriptEnabled(true);
                webiew.getSettings().setPluginState(WebSettings.PluginState.ON);
                webiew.loadUrl(insta);
                webiew.setWebChromeClient(new WebChromeClient());
            }
        });

        Mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webiew.getSettings().setJavaScriptEnabled(true);
                webiew.getSettings().setPluginState(WebSettings.PluginState.ON);
                webiew.loadUrl(web);
                webiew.setWebChromeClient(new WebChromeClient());
            }
        });

        return v;
    }

    public void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL+"App/sociallinks"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("true")) {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        JSONObject jsonObj=jsonObject.getJSONObject("bar");

                        Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
                        Face=jsonObj.getString("facebook");
                        Tweet=jsonObj.getString("twitter");
                        insta=jsonObj.getString("instagram");
                        web = jsonObj.getString("website");


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
