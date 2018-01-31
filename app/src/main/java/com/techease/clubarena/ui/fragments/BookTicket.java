package com.techease.clubarena.ui.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookTicket extends Fragment {

    @BindView(R.id.et_holder_name)
    EditText et_holder_name ;

    @BindView(R.id.tv_event_name)
    TextView tv_event_name;

    @BindView(R.id.tv_event_address)
    TextView tv_event_address;

    @BindView(R.id.event_image)
    ImageView event_image ;

    @BindView(R.id.tv_tickets_needed)
            TextView tv_tickets_needed ;

    @BindView(R.id.tv_ticket_minus)
            TextView tv_ticket_minus ;

    @BindView(R.id.tv_ticket_plus)
            TextView tv_ticket_plus ;

    @BindView(R.id.tv_total_cost)
            TextView tv_total_cost ;

    @BindView(R.id.btn_book_ticket)
    Button btn_book_ticket ;

    @BindView(R.id.holder_name)
            TextView holder_name ;

    @BindView(R.id.tkt_needed)
            TextView tkt_nneded ;

    @BindView(R.id.total_cost)
            TextView total_cost ;

    @BindView(R.id.checkbox)
    CheckBox checkBox ;


    Typeface extra_bold, bold, light ;
    Unbinder unbinder;
    String event_id ,name ,tkt_price   , event_name , location , picture;
    int cost =1 , tickets = 1, costt ;
    android.support.v7.app.AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_book_ticket, container, false);
        unbinder = ButterKnife.bind(this,v);

        event_id = getArguments().getString("event_id");
        tkt_price = getArguments().getString("tkt_price");
        event_name = getArguments().getString("event_name");
        location = getArguments().getString("location");
        picture = getArguments().getString("picture");

        cost = Integer.parseInt(tkt_price);

        extra_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-ExtraBold.ttf");
        bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Bold.ttf");
        light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Regular.ttf");

        tv_event_name.setTypeface(extra_bold);
        tv_event_address.setTypeface(bold);
        et_holder_name.setTypeface(light);
        tv_tickets_needed.setTypeface(light);
        tv_ticket_minus.setTypeface(extra_bold);
        tv_ticket_plus.setTypeface(extra_bold);
        tv_total_cost.setTypeface(light);
        holder_name.setTypeface(extra_bold);
        tkt_nneded.setTypeface(extra_bold);
        total_cost.setTypeface(extra_bold);

        Glide.with(getActivity()).load(picture).into(event_image);
        tv_event_name.setText(event_name);
        tv_event_address.setText(location);
        tv_total_cost.setText("$"+tkt_price);

        if(InternetUtils.isNetworkConnected(getActivity()))
        {

            btn_book_ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDataInput();
                }
            });

        }
        else
        {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        tv_ticket_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tickets++ ;
                tv_tickets_needed.setText(String.valueOf(tickets));
                costt = cost * tickets ;
                tv_total_cost.setText(String.valueOf("$"+costt));
            }
        });

        tv_ticket_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tickets > 1) {
                    tickets--;
                    tv_tickets_needed.setText(String.valueOf(tickets));
                    costt = cost * tickets;
                    tv_total_cost.setText(String.valueOf("$" + costt));
                }
            }
        });


        return  v;
    }

    public void onDataInput() {
        name = et_holder_name.getText().toString();
        if (name.equals("")) {
            et_holder_name.setError("Please enter your Name");

        }else if (! checkBox.isChecked()){
                    checkBox.setError("Please assure that you are elder then 18 yrs");
        } else {
            if (alertDialog == null)
                alertDialog = AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();
            apicall();
        }
    }


    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.USER_URL+"App/bookEvent"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true")) {
                        if (alertDialog != null)
                            alertDialog.dismiss();

                    Fragment fragment=new ConfirmBooking();
                    Bundle bundle = new Bundle();
                    bundle.putString("holder", name  );
                    bundle.putString("tickets", String.valueOf(tickets));
                    bundle.putString("cost", String.valueOf(costt));
                    fragment.setArguments(bundle);
                    getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_main,fragment).addToBackStack("").commit();


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
                params.put("name", name);
                params.put("tickets", String.valueOf(tickets));
                params.put("cost", String.valueOf(cost));
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
