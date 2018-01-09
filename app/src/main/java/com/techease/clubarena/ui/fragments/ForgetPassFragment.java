package com.techease.clubarena.ui.fragments;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techease.clubarena.R;
import com.techease.clubarena.utils.AlertsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.Unbinder;


public class ForgetPassFragment extends Fragment {

    @BindView(R.id.et_email_forget)
    EditText et_email_forget ;

    @BindView(R.id.btn_forget)
    Button btn_forget ;

    Unbinder unbinder;
    String strEmail ;

    Fragment fragment;
    android.support.v7.app.AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_forget_pass, container, false);
        unbinder = ButterKnife.bind(this, v);



        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDataInput();


            }
        });
        return v;

    }

    public void onDataInput() {
        strEmail = et_email_forget.getText().toString();

        if ((!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())) {
            et_email_forget.setError("Please enter valid email id");
        } else {
            if (alertDialog == null){
                alertDialog = AlertsUtils.createProgressDialog(getActivity());
                alertDialog.show();
            }
            apiCall();
        }
    }

    public void apiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://barapp.adadigbomma.com/Signup/forgot", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("true")) {

                    if (alertDialog != null)
                        alertDialog.dismiss();
                    fragment = new VerifyCodeFragment();
                    Bundle args = new Bundle();
                    args.putString("email", strEmail);
                    fragment.setArguments(args);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack("tag").commit();


                }else{
                    JSONObject jsonObject = null;
                    try {
                        if (alertDialog != null)
                        alertDialog.dismiss();
                        jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        AlertsUtils.showErrorDialog(getActivity(), message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog != null)
                    alertDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Connection Error");
                } else if (error instanceof AuthFailureError) {

                    AlertsUtils.showErrorDialog(getActivity(), "Auth Failure");
                } else if (error instanceof ServerError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Server Error");
                } else if (error instanceof NetworkError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Network Error");
                } else if (error instanceof ParseError) {
                    AlertsUtils.showErrorDialog(getActivity(), "Parse Error");
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", strEmail);
                params.put("Accept", "application/json");
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
