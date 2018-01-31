package com.techease.clubarena.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.techease.clubarena.R;
import com.techease.clubarena.utils.AlertsUtils;
import com.techease.clubarena.utils.Configuration;
import com.techease.clubarena.utils.InternetUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class EventDetails extends Fragment {

    @BindView(R.id.tv_event_name)
    TextView tv_event_name ;

    @BindView(R.id.tv_event_date)
            TextView tv_event_date ;

    @BindView(R.id.tv_event_time)
            TextView tv_event_time ;

    @BindView(R.id.tv_event_address)
            TextView tv_event_address;

    @BindView(R.id.tv_event_about)
            TextView tv_event_about ;

    @BindView(R.id.tv_avl_tickets)
    TextView ticket_avl ;

    @BindView(R.id.tv_ticket_price)
    TextView tkt_price ;

    @BindView(R.id.btn_book_ticket)
    Button btn_book_tkt ;

    @BindView(R.id.event_image)
    ImageView event_image ;

    Typeface extra_bold, bold, light ;
    String event_id , price_ticket, event_name, location, picture;


    Unbinder unbinder;
    android.support.v7.app.AlertDialog alertDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_details, container, false);
        unbinder = ButterKnife.bind(this,v);

        extra_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-ExtraBold.ttf");
        bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Bold.ttf");
        light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Regular.ttf");

        tv_event_name.setTypeface(extra_bold);
        tv_event_date.setTypeface(extra_bold);
        tv_event_time.setTypeface(bold);
        tv_event_address.setTypeface(light);
        tv_event_about.setTypeface(light);
        ticket_avl.setTypeface(light);
        tkt_price.setTypeface(light);

        event_id = getArguments().getString("event_id");

        if(InternetUtils.isNetworkConnected(getActivity()))
        {

            apicall();
            if (alertDialog == null)
                alertDialog = AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();

        }
        else
        {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }


        btn_book_tkt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new BookTicket();
                Bundle bundle = new Bundle();
                bundle.putString("event_id", event_id  );
                bundle.putString("tkt_price", price_ticket);
                bundle.putString("event_name", event_name);
                bundle.putString("location", location);
                bundle.putString("picture", picture);
                fragment.setArguments(bundle);
                getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_main,fragment).commit();
            }
        });

        return v ;
    }



    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL+"App/eventDetail"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                    try {
                        if (alertDialog != null)
                            alertDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArr = jsonObject.getJSONArray("events");
                        for (int i=0; i<jsonArr.length(); i++)
                        {
                            JSONObject temp = jsonArr.getJSONObject(i);

                            event_id = temp.getString("event_id");
                            String bar_name = temp.getString("bar_name");
                             event_name =temp.getString("event_name");
                            picture =temp.getString("picture");
                            String date =temp.getString("date");
                            String time_from =temp.getString("time_from");
                            String time_to =temp.getString("time_to");
                            String about =temp.getString("about");
                             location =temp.getString("location");
                            String total_tickets =temp.getString("total_tickets");
                            price_ticket =temp.getString("price_ticket");

                            tv_event_name.setText(event_name);
                            tv_event_date.setText(date);
                            tv_event_time.setText(time_from + " - " + time_to);
                            tv_event_address.setText(location);
                            tv_event_about.setText(about);
                            ticket_avl.setText(total_tickets);
                            tkt_price.setText(price_ticket);
                            Glide.with(getActivity()).load(picture).into(event_image);


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

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("event_id", event_id);
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
