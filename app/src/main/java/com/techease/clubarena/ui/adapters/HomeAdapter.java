package com.techease.clubarena.ui.adapters;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techease.clubarena.R;
import com.techease.clubarena.models.HomeModel;
import com.techease.clubarena.utils.Configuration;

import java.util.List;

/**
 * Created by kaxhiftaj on 1/8/18.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{

    Context context ;
    List<HomeModel> home_models_list;


    public HomeAdapter(Context context, List<HomeModel> models) {
        this.context=context;
        this.home_models_list = models;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_home,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HomeModel home_model = home_models_list.get(position);
        holder.club_name.setText(home_model.getClub_name());
        holder.club_rating_bar.setRating(Float.valueOf(home_model.getRating()));
        holder.club_rating_bar.getSolidColor();
        Glide.with(context).load(home_model.getImage_url()).into(holder.iv_club_image);
    }



    @Override
    public int getItemCount() {
        return home_models_list.size();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_club_image;
        TextView club_name;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        String api_token,Image_Url;
        Typeface typeface,typeface2;
        RatingBar club_rating_bar ;
        public MyViewHolder(View itemView) {
            super(itemView);
            sharedPreferences = context.getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();

            iv_club_image=(ImageView)itemView.findViewById(R.id.iv_club_image);
            club_name = (TextView)itemView.findViewById(R.id.tv_club_name);
            club_rating_bar = (RatingBar)itemView.findViewById(R.id.club_rating_bar);

         //   typeface = Typeface.createFromAsset(context.getAssets(),"font/brandon_blk.otf");
         //   typeface2 = Typeface.createFromAsset(context.getAssets(),"font/brandon_reg.otf");



        }

    }
}
