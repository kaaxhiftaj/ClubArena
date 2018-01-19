package com.techease.clubarena.ui.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.techease.clubarena.utils.AlertsUtils;
import com.techease.clubarena.utils.Configuration;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class VerifyCodeFragment extends Fragment {


    EditText et_num1,et_num2,et_num3,et_num4,et_num5,et_num6;
    String strVerifyCode;
    Button btn_verify_code;
    Fragment fragment ;
    String email;
    android.support.v7.app.AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_code, container, false);

        btn_verify_code = view.findViewById(R.id.btn_verify_code);
        Bundle args = getArguments();
        email = args.getString("email");


            et_num1 = view.findViewById(R.id.et_code_num1);
            et_num2 = view.findViewById(R.id.et_code_num2);
            et_num3 = view.findViewById(R.id.et_code_num3);
            et_num4 = view.findViewById(R.id.et_code_num4);
            et_num5 = view.findViewById(R.id.et_code_num5);
            et_num6 = view.findViewById(R.id.et_code_num6);


            et_num1.addTextChangedListener(genraltextWatcher);
            et_num2.addTextChangedListener(genraltextWatcher);
            et_num3.addTextChangedListener(genraltextWatcher);
            et_num4.addTextChangedListener(genraltextWatcher);
            et_num5.addTextChangedListener(genraltextWatcher);
            et_num6.addTextChangedListener(genraltextWatcher);



            btn_verify_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String num1 = et_num1.getText().toString();
                    String num2 = et_num2.getText().toString();
                    String num3 = et_num3.getText().toString();
                    String num4 = et_num4.getText().toString();
                    String num5 = et_num5.getText().toString();
                    String num6 = et_num6.getText().toString();

                    strVerifyCode = num1+num2+num3+num4+num5+num6;

                }
            });



            return view;
        }
        private TextWatcher genraltextWatcher = new TextWatcher() {
            private View view;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (et_num1.length()==1) {

                    et_num2.requestFocus();

                }if (et_num2.length()==1){

                    et_num3.requestFocus();

                }if (et_num3.length()==1) {

                    et_num4.requestFocus();

                }if (et_num4.length()==1){

                    et_num5.requestFocus();

                }if (et_num5.length()==1){

                    et_num6.requestFocus();

                }if (et_num6.length()==1){

                    strVerifyCode = et_num1.getText().toString()+et_num2.getText().toString()+et_num3.getText().toString()+
                             et_num4.getText().toString()+et_num5.getText().toString()+et_num6.getText().toString() ;

                    onDataInput();

                }

            }

        };

    public void onDataInput() {

        if (strVerifyCode.equals("") || strVerifyCode.length() < 6) {
            et_num6.setError("Please enter a valid code");
        } else {
            if (alertDialog == null){
                alertDialog = AlertsUtils.createProgressDialog(getActivity());
                alertDialog.show();
            }
            apiCall();
        }
    }

    public void apiCall() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL+"Signup/CheckCode/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("true")){
                            if (alertDialog != null)
                                alertDialog.dismiss();
                                Bundle args = new Bundle();
                                fragment = new ResetPassFragment();
                                args.putString("code", strVerifyCode);
                                 fragment.setArguments(args);
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction transaction = fm.beginTransaction();
                                transaction.replace(R.id.fragment_container, fragment);
                                transaction.addToBackStack("tag").commit();



                        }else {

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
                params.put("email", email);
                params.put("code", strVerifyCode);
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

