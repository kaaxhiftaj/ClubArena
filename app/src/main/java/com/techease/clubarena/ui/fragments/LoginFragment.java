package com.techease.clubarena.ui.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.techease.clubarena.ui.activities.MainActivity;
import com.techease.clubarena.utils.Configuration;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;



public class LoginFragment extends Fragment  {

    @BindView(R.id.et_email_signin)
    EditText et_email_signin ;

    @BindView(R.id.et_password_signin)
    EditText et_password_signin ;

    @BindView(R.id.btn_signin)
    Button btn_signin ;

    @BindView(R.id.tv_register)
    TextView tv_register;

    @BindView(R.id.tv_forgot)
            TextView tv_forgot ;

    Unbinder unbinder ;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strEmail, strPassword;
    String device_token ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, v);
        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        device_token = sharedPreferences.getString("device_token","");

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new RegisterFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack("tag").commit();
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDataInput();
            }
        });

        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ForgetPassFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack("tag").commit();
            }
        });

        return v ;
    }



    public void onDataInput() {
        strEmail = et_email_signin.getText().toString();
        strPassword = et_password_signin.getText().toString();
        if ((!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())) {
            et_email_signin.setError("Please enter valid email id");
        } else if (strPassword.equals("")) {
            et_password_signin.setError("Please enter your password");
        } else {
        //    DialogUtils.showProgressSweetDialog(getActivity(), "Getting Login");
            apiCall();
        }
    }

    public void apiCall() {
//        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#7DB3D2"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(false);
//        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,  "http://barapp.adadigbomma.com/Signup/login"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zma log ", response);
      //          DialogUtils.sweetAlertDialog.dismiss();

                if (response.contains("true")) {
                    try {
                        Log.d("zma log inner ", response);
                        JSONObject jsonObject = new JSONObject(response).getJSONObject("user");
                        String strApiToken = jsonObject.getString("token");
                        String Logged_In_User_Id=jsonObject.getString("user_id");
                        String fullname = jsonObject.getString("user_name");
                        String email = jsonObject.getString("email");
                        editor.putString("api_token", strApiToken);
                        editor.putString("user_Id",Logged_In_User_Id);
                        editor.putString("user_name" , fullname);
                        editor.putString("email" , email);
                        editor.commit();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("error", String.valueOf(e.getMessage()));
                    }
                    //  pDialog.dismiss();
                    editor.putString("api_token", "abc").commit();


                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
          //      DialogUtils.sweetAlertDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
          //          DialogUtils.showWarningAlertDialog(getActivity(), "Network Error");
                } else if (error instanceof AuthFailureError) {
          //          DialogUtils.showWarningAlertDialog(getActivity(), "Email or Password Error");
                } else if (error instanceof ServerError) {
          //          DialogUtils.showWarningAlertDialog(getActivity(), "Server Error");
                } else if (error instanceof NetworkError) {
         //           DialogUtils.showWarningAlertDialog(getActivity(), "Network Error");
                } else if (error instanceof ParseError) {
         //           DialogUtils.showWarningAlertDialog(getActivity(), "Parsing Error");
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
                params.put("password", strPassword);
                //   params.put("Accept", "application/json");
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
