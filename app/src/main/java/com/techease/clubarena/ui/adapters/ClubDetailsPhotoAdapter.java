package com.techease.clubarena.ui.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.techease.clubarena.R;
import com.techease.clubarena.models.ClubDetailsPhotoModel;
import com.techease.clubarena.models.ModelClubDetails;
import com.techease.clubarena.ui.fragments.ClubDetailPhotos;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by kaxhiftaj on 1/15/18.
 */

public class ClubDetailsPhotoAdapter  extends  BaseAdapter{



        private List<ClubDetailsPhotoModel> contestents;
        private Context context;
        private LayoutInflater layoutInflater;
        MyViewHolder viewHolder = null;
        public ClubDetailsPhotoAdapter(Context context, List<ClubDetailsPhotoModel> contestents) {

            this.context = context;
            this.contestents = contestents ;
            if (context!=null)
            {
                this.layoutInflater=LayoutInflater.from(context);
            }
        }

        @Override
        public int getCount() {
            if (contestents!=null) return contestents.size();
            return 0;
        }

        @Override
        public Object getItem(int i) {
            if(contestents != null && contestents.size() > i) return  contestents.get(i);
            return null;
        }

        @Override
        public long getItemId(int i) {
            final ClubDetailsPhotoModel model=contestents.get(i);
            if(contestents != null && contestents.size() > i) return  contestents.size();
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final ClubDetailsPhotoModel model = contestents.get(i);
            viewHolder=new MyViewHolder() ;
            view=layoutInflater.inflate(R.layout.custom_clubdetail_images, viewGroup,false);
            viewHolder.imageView=(ImageView)view.findViewById(R.id.iv_clubdetails_image);
            Glide.with(context).load(model.getImage_url()).into(viewHolder.imageView);
            final Drawable drawable=viewHolder.imageView.getDrawable();

            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.custom_image_display);
                    dialog.setCancelable(true);
                    ImageView img = (ImageView) dialog.findViewById(R.id.image_display);
                    Glide.with(context).load(model.getImage_url()).into(img);
                    dialog.show();
                }
            });
            view.setTag(viewHolder);

            return view;
        }


        public class MyViewHolder {

            ImageView imageView;

        }


    }

