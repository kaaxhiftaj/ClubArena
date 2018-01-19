package com.techease.clubarena.ui.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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
import com.techease.clubarena.utils.Configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ResetPassFragment extends Fragment {

    @BindView(R.id.et_pass_reset)
    EditText et_pass_reset;

    @BindView(R.id.et_passmatch_reset)
    EditText et_passmatch_reset ;

    @BindView(R.id.btn_reset)
    Button btn_reset ;
    String strPassword , strNewPasswordMatch, code ;
    android.support.v7.app.AlertDialog alertDialog;


    Unbinder unbinder ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reset_pass, container, false);
        unbinder = ButterKnife.bind(this, v);

        Bundle args = getArguments();
        code = args.getString("code");

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDataInput();
            }
        });
        return v;
    }


    public void onDataInput() {

        strPassword = et_pass_reset.getText().toString();
        strNewPasswordMatch = et_passmatch_reset.getText().toString();

        if (strPassword.equals("")) {
            et_pass_reset.setError("Please enter a valid new password");
        } else if (et_passmatch_reset.equals(strPassword)) {
            et_passmatch_reset.setError("Password does'nt match");
        }  else {

            if (alertDialog == null){
                alertDialog = AlertsUtils.createProgressDialog(getActivity());
                alertDialog.show();
            }
            apiCall();
        }
    }


    public void apiCall() {

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL+"Signup/Resetpassword",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("true")) {
                            if (alertDialog != null)
                                alertDialog.dismiss();
                            Fragment fragment = new LoginFragment();
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction transaction = fm.beginTransaction();
                            transaction.replace(R.id.fragment_container, fragment);
                            transaction.addToBackStack("tag").commit();

                        } else {

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
                params.put("code", code);
                params.put("password", strPassword);
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
