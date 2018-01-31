package com.techease.clubarena.ui.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.techease.clubarena.models.ModelClubDetails;
import com.techease.clubarena.ui.adapters.ClubDetailsPhotoAdapter;
import com.techease.clubarena.utils.AlertsUtils;
import com.techease.clubarena.utils.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.LongToDoubleFunction;

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

    @BindView(R.id.iv_call)
    ImageButton iv_call;

    @BindView(R.id.iv_email)
    ImageButton iv_email;

    @BindView(R.id.iv_web)
    ImageButton iv_web ;

    @BindView(R.id.iv_share)
    ImageButton iv_share;

    @BindView(R.id.iv_fav)
    ImageButton iv_fav;

    String phone_no, website, email ;
    Typeface custom_font ;
    String club_id ;
    String user_id ;
    Boolean isfav ;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_club_details, container, false);
        unbinder = ButterKnife.bind(this, v);
        custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-ExtraBold.ttf");
        club_id = getArguments().getString("club_id");

        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        user_id = sharedPreferences.getString("user_id","");
//        club_id = sharedPreferences.getString("club_id","");

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
        setCustomFont();
        apicall();
        if (alertDialog == null)
            alertDialog = AlertsUtils.createProgressDialog(getActivity());
        alertDialog.show();

        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_no));
                startActivity(intent);
            }
        });

        iv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Query");
                intent.putExtra(Intent.EXTRA_TEXT, "Please write your email here");
                intent.setData(Uri.parse("mailto:"+email)); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);

            }

        });

        iv_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        iv_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialog == null)
                    alertDialog = AlertsUtils.createProgressDialog(getActivity());
                alertDialog.show();

                favapicall();

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


    public class PagerAdapter extends FragmentStatePagerAdapter {
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
                    Bundle bundle = new Bundle();
                    bundle.putString("club_id", club_id );
                    frag.setArguments(bundle);
                    return frag;

                case 1:
                    ClubDetailPhotos frag1 = new ClubDetailPhotos();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("club_id", club_id );
                    frag1.setArguments(bundle1);
                    return frag1;
                case 2:
                    ClubDetailVideos frag2 = new ClubDetailVideos();
                    Bundle bundl2 = new Bundle();
                    bundl2.putString("club_id", club_id );
                    frag2.setArguments(bundl2);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL+"App/clubdetail"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    try {
                        if (alertDialog != null)
                            alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                            JSONObject temp = jsonObject.getJSONObject("bar");
                            String name = temp.getString("name");
                            phone_no = temp.getString("contact_number");
                            website = temp.getString("website");
                             email = temp.getString("email");



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
                params.put("club_id", club_id );

                return params;
            }

        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }


    public void setCustomFont(){
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {

                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-ExtraBold.ttf"));
                }
            }
        }
    }






    private void favapicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL+"App/favorite_bar"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {

                        if (alertDialog != null)
                            alertDialog.dismiss();
                    Toast.makeText(getActivity(), "Added to Favourites", Toast.LENGTH_SHORT).show();
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
                params.put("club_id", club_id );
                params.put("user_id", user_id);

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
