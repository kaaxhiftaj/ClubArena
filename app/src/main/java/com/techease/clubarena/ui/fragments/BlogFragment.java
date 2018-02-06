package com.techease.clubarena.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.techease.clubarena.utils.AlertsUtils;
import com.techease.clubarena.utils.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BlogFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<BlogModel> blogModels;
    BlogAdapter blogAdapter;
    android.support.v7.app.AlertDialog alertDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blog, container, false);

        recyclerView=(RecyclerView) v.findViewById(R.id.rvBlog);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (alertDialog == null)
            alertDialog = AlertsUtils.createProgressDialog(getActivity());
        apicall();
        blogModels=new ArrayList<>();
        blogAdapter=new BlogAdapter(getActivity(),blogModels);
        recyclerView.setAdapter(blogAdapter);
        return v;
    }

    private void apicall() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Configuration.USER_URL+"App/blog"
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("zma  reg response", response);
                try {
                    JSONObject jsonObject1=new JSONObject(response);
                    JSONArray jsonArray=jsonObject1.getJSONArray("user ");

                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        BlogModel bModel=new BlogModel();
                        bModel.setBlogId(jsonObject.getString("id"));
                        bModel.setBlogTitle(jsonObject.getString("title"));
                        bModel.setBlogDes(jsonObject.getString("description"));
                        bModel.setBlogLink(jsonObject.getString("link"));
                        bModel.setBlogImageLink(jsonObject.getString("image"));
                        blogModels.add(bModel);
                        if (alertDialog != null)
                            alertDialog.dismiss();
                    }
                    blogAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                    if (alertDialog != null)
                        alertDialog.dismiss();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog != null)
                    alertDialog.dismiss();
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
