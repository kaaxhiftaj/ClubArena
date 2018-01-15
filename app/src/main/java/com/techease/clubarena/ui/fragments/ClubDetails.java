package com.techease.clubarena.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.techease.clubarena.models.ModelClubDetails;
import com.techease.clubarena.ui.adapters.ClubDetailsPhotoAdapter;
import com.techease.clubarena.utils.AlertsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ClubDetails extends Fragment {

    @BindView(R.id.club_details_tab)
    TabLayout tabLayout ;

    @BindView(R.id.club_details_pager)
    ViewPager viewPager;

    Unbinder unbinder;
    android.support.v7.app.AlertDialog alertDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_club_details, container, false);
        unbinder = ButterKnife.bind(this, v);

        apicall();
        if (alertDialog == null)
            alertDialog = AlertsUtils.createProgressDialog(getActivity());
        alertDialog.show();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tabLayout.addTab(tabLayout.newTab().setText("INFO"));
        tabLayout.addTab(tabLayout.newTab().setText("PHOTOS"));
        tabLayout.addTab(tabLayout.newTab().setText("VIDEOS"));
        viewPager.setAdapter(new PagerAdapter(((FragmentActivity)getActivity()).getSupportFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        reduceMarginsInTabs(tabLayout,20);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return  v ;
    }



    public static void reduceMarginsInTabs(TabLayout tabLayout, int marginOffset) {

        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            for (int i = 0; i < ((ViewGroup) tabStrip).getChildCount(); i++) {
                View tabView = tabStripGroup.getChildAt(i);
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ((ViewGroup.MarginLayoutParams) tabView.getLayoutParams()).leftMargin = marginOffset;
                    ((ViewGroup.MarginLayoutParams) tabView.getLayoutParams()).rightMargin = marginOffset;
                }
            }

            tabLayout.requestLayout();
        }
    }


    public static class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(android.support.v4.app.FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }



        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            switch (position) {
                case 0:
                    ClubDetailsInfo frag = new ClubDetailsInfo();
                    return frag;

                case 1:
                    ClubDetailPhotos frag1 = new ClubDetailPhotos();
                    return frag1;
                case 2:
                    ClubDetailVideos frag2 = new ClubDetailVideos();
                    return frag2;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }



    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://barapp.adadigbomma.com/App/clubdetails"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    try {
                        if (alertDialog != null)
                            alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("zma resp", String.valueOf(jsonObject));
                        JSONArray jsonArr = jsonObject.isNull("bar")?null: jsonObject.getJSONArray("bar");
                        for (int i=0; i<jsonArr.length(); i++)
                        {

                            ModelClubDetails clubDetails = new ModelClubDetails();

                            JSONObject temp = jsonArr.getJSONObject(i);
                            String name = temp.getString("name");
                            String latitude = temp.getString("latitude");
                            String longitude = temp.getString("longitude");
                            String image = temp.getString("image");
                            String open_time = temp.getString("open_time");
                            String close_time = temp.getString("close_time");
                            String club_type = temp.getString("club_type");


                            clubDetails.setImage_url(image);
                         //   list.add(clubDetails);

                            JSONObject temp1 = temp.getJSONObject("info");
                            String info = temp1.getString("information");
                            String contact_number = temp1.getString("contact_number");
                            String website = temp1.getString("website");
                            String email = temp1.getString("email");


                            JSONArray jsonArray = temp.getJSONArray("bar_images");
                            for (int j =0 ; j < jsonArray.length(); j++){
                                String obj = jsonArray.getString(i);


                            }
                       //     clubDetailsPhotoAdapter=new ClubDetailsPhotoAdapter(getActivity(),list);



                            JSONArray jsonArray1 = temp.getJSONArray("bar_videos");
                            for (int j =0 ; j < jsonArray.length(); j++){
                                String obj = jsonArray.getString(i);


                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (alertDialog != null)
                            alertDialog.dismiss();
                    }
                }
                else {
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
                params.put("club_id", "2");

                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }



}
