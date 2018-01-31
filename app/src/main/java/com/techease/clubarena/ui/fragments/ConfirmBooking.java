package com.techease.clubarena.ui.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.techease.clubarena.R;
import com.techease.clubarena.utils.InternetUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ConfirmBooking extends Fragment {


    @BindView(R.id.et_holder_name)
    TextView et_holder_name ;

    @BindView(R.id.tv_tickets_needed)
    TextView tv_tickets_needed ;

    @BindView(R.id.tv_total_cost)
    TextView tv_total_cost ;


    @BindView(R.id.holder_name)
    TextView holder_name ;

    @BindView(R.id.tkt_needed)
    TextView tkt_nneded ;

    @BindView(R.id.total_cost)
    TextView total_cost ;

    @BindView(R.id.done)
            Button done ;

    Typeface extra_bold, bold, light ;
    Unbinder unbinder;
    String holder, tickets , cost;
    android.support.v7.app.AlertDialog alertDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_confirm_booking, container, false);
        unbinder = ButterKnife.bind(this, v);

        extra_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-ExtraBold.ttf");
        bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Bold.ttf");
        light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Regular.ttf");


        et_holder_name.setTypeface(light);
        tv_tickets_needed.setTypeface(light);
        tv_total_cost.setTypeface(light);
        holder_name.setTypeface(extra_bold);
        tkt_nneded.setTypeface(extra_bold);
        total_cost.setTypeface(extra_bold);




        if(InternetUtils.isNetworkConnected(getActivity()))
        {

            holder = getArguments().getString("holder");
            tickets = getArguments().getString("tickets");
            cost = getArguments().getString("cost");

            et_holder_name.setText(holder);
            tv_total_cost.setText("$"+cost);
            tv_tickets_needed.setText(tickets);

        }
        else
        {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new EventFragment();
                getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_main,fragment).commit();
            }
        });

        return v ;
    }

}
