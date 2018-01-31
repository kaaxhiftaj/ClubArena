package com.techease.clubarena.ui.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.techease.clubarena.R;
import com.techease.clubarena.models.EventModel;
import com.techease.clubarena.models.HomeModel;
import com.techease.clubarena.ui.activities.MainActivity;
import com.techease.clubarena.ui.fragments.ClubDetails;
import com.techease.clubarena.ui.fragments.EventDetails;
import com.techease.clubarena.utils.Configuration;

import java.util.List;

/**
 * Created by kaxhiftaj on 1/8/18.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder>{

    Context context ;
    List<EventModel> event_models_list;
    String club_id ;


    public EventAdapter(Context context, List<EventModel> models) {
        this.context=context;
        this.event_models_list = models;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_events,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final EventModel event_model = event_models_list.get(position);
        holder.club_name.setText(event_model.getClub_name());
        holder.event_name.setText(event_model.getEvent_name());
        club_id = event_model.getId();
        Glide.with(context).load(event_model.getImage_url()).into(holder.iv_club_image);

        holder.iv_club_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment=new EventDetails();
                Bundle bundle = new Bundle();
                bundle.putString("event_id",  event_model.getId());
                fragment.setArguments(bundle);
                Activity activity=(MainActivity)context;
                activity.getFragmentManager().beginTransaction().replace(R.id.fragment_main,fragment).addToBackStack("").commit();
            }
        });

    }



    @Override
    public int getItemCount() {
        return event_models_list.size();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_club_image;
        TextView club_name, event_name, club_distance;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        Typeface typeface;
        LinearLayout custom_home_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            sharedPreferences = context.getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            club_id = sharedPreferences.getString("club_id","");
            custom_home_layout = (LinearLayout) itemView.findViewById(R.id.custom_home_layout);
            iv_club_image = (ImageView) itemView.findViewById(R.id.iv_club_image);
            club_name = (TextView) itemView.findViewById(R.id.tv_club_name);
            club_distance = (TextView)itemView.findViewById(R.id.tv_club_distance);
            event_name = (TextView)itemView.findViewById(R.id.tv_event_name);

            typeface = Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Bold.ttf");
            club_name.setTypeface(typeface);
            club_distance.setTypeface(typeface);




        }


    }
}
