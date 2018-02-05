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
import com.techease.clubarena.models.HomeModel;
import com.techease.clubarena.ui.activities.MainActivity;
import com.techease.clubarena.ui.fragments.ClubDetails;
import com.techease.clubarena.utils.Configuration;

import java.util.List;

/**
 * Created by kaxhiftaj on 1/8/18.
 */

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder>{

    Context context ;
    List<HomeModel> home_models_list;
    String club_id ;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public FavAdapter(Context context, List<HomeModel> models) {
        this.context=context;
        this.home_models_list = models;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_favourite,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        sharedPreferences = context.getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        final HomeModel home_model = home_models_list.get(position);
        holder.club_name.setText(home_model.getClub_name());
        club_id = home_model.getId();
        holder.club_rating_bar.setRating(Float.valueOf(home_model.getRating()));
        holder.club_rating_bar.getSolidColor();
        Glide.with(context).load(home_model.getImage_url()).into(holder.iv_club_image);

        holder.iv_club_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment=new ClubDetails();
                Bundle bundle = new Bundle();
                bundle.putString("club_id",  home_model.getId());
            //    editor.putString("club_id", club_id).commit();
                Toast.makeText(context, home_model.getId(), Toast.LENGTH_SHORT).show();
                fragment.setArguments(bundle);
                Activity activity=(MainActivity)context;
                activity.getFragmentManager().beginTransaction().replace(R.id.fragment_main,fragment).addToBackStack("").commit();
            }
        });

    }



    @Override
    public int getItemCount() {
        return home_models_list.size();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_club_image;
        TextView club_name, club_distance;
        RatingBar club_rating_bar;
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
            club_rating_bar = (RatingBar) itemView.findViewById(R.id.club_rating_bar);

            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway_Bold.ttf");
            club_name.setTypeface(typeface);
            club_distance.setTypeface(typeface);




        }


    }
}
