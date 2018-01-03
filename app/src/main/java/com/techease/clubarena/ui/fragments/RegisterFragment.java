package com.techease.clubarena.ui.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.techease.clubarena.utils.Configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class RegisterFragment extends Fragment {


    @BindView(R.id.et_username_signup)
    EditText et_username_signnup;

    @BindView(R.id.et_email_signup)
    EditText et_email_signup;

    @BindView(R.id.et_password_signup)
    EditText et_password_signup ;

    @BindView(R.id.btn_signup)
    Button btn_signup;

    @BindView(R.id.tv_signin)
    TextView tv_signin;

@BindView(R.id.pd)
        ProgressBar pd ;

    Unbinder unbinder ;
    String  strUserName, strEmail, strPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String device_token ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, v);

        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        device_token = sharedPreferences.getString("device_token","");

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Raleway-ExtraBold.ttf");
        tv_signin.setTypeface(custom_font);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               onDataInput();
            }
        });

        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new LoginFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack("tag").commit();
            }
        });

        return v;
    }



    public void onDataInput() {
        strUserName = et_username_signnup.getText().toString();
        strEmail = et_email_signup.getText().toString();
        strPassword = et_password_signup.getText().toString();

        if (strUserName.equals("") || strEmail.length() < 3) {
            et_username_signnup.setError("Enter a valid Name");
        } else if ((!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())) {
            et_email_signup.setError("Please enter valid email id");
        } else if (strPassword.length() < 6 ) {
            et_password_signup.setError("Please enter a scure password");
        } else {

            apiCall();


        }

    }
//

    public void apiCall() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://barapp.adadigbomma.com/Signup/register", new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d("zma  reg response", response);
                DialogUtils.sweetAlertDialog.dismiss();
                if (response.contains("true")) {

                    try {
                        JSONObject jsonObject = new JSONObject(response).getJSONObject("user");
                        String strApiToken = jsonObject.getString("token");
                        Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
                        Fragment  fragment = new LoginFragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        Toast.makeText(getActivity(), message , Toast.LENGTH_SHORT).show();
                        DialogUtils.showErrorDialog(getActivity(), message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
       //         DialogUtils.sweetAlertDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
      //              DialogUtils.showWarningAlertDialog(getActivity(), "Network Error");
                } else if (error instanceof AuthFailureError) {
      //              DialogUtils.showWarningAlertDialog(getActivity(), "Email or Password Error");
                } else if (error instanceof ServerError) {
       //             DialogUtils.showWarningAlertDialog(getActivity(), "Server Error");
                } else if (error instanceof NetworkError) {
        //            DialogUtils.showWarningAlertDialog(getActivity(), "Network Error");
                } else if (error instanceof ParseError) {
        //            DialogUtils.showWarningAlertDialog(getActivity(), "Parsing Error");
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
                params.put("fullname", strUserName);
                params.put("email", strEmail);
                params.put("password", strPassword);
                params.put("device", "android");
                params.put("device_id" , "");
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
