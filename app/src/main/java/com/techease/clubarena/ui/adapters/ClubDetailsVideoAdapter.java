package com.techease.clubarena.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.techease.clubarena.R;
import com.techease.clubarena.models.ClubDetailsVideoModel;

import java.util.List;

/**
 * Created by kaxhiftaj on 1/16/18.
 */

public class ClubDetailsVideoAdapter extends RecyclerView.Adapter<ClubDetailsVideoAdapter.MyViewHolder> {


        List<ClubDetailsVideoModel> videoModel;
        Context context;
        public static final String Key="AIzaSyB-nlOb637v1IdygX_9_iyhlTpPgrLRQ1A";
        public static String Id_s;
        public ClubDetailsVideoAdapter(Context context, List<ClubDetailsVideoModel> model){
            this.context=context;
            this.videoModel=model;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_clubdetail_videos, parent, false);
            return new MyViewHolder(rootView);
        }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ClubDetailsVideoModel model = videoModel.get(position);
        holder.text.setText(model.getTitle());
        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener= new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.playButton.setVisibility(View.VISIBLE);
            }
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
            }
        };
        holder.youTubeThumbnailView.initialize(Key, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                Id_s = model.getId();
                youTubeThumbnailLoader.setVideo(model.getId());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }
            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
            }
        });
        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(context).equals(YouTubeInitializationResult.SUCCESS)){
                    //This means that your device has the Youtube API Service (the app) and you are safe to launch it.
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, Key,model.getId());
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, "Please download youtube app", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.youtube")));
                }
            }
        });
    }


        @Override
        public int getItemCount() {
            return videoModel.size();
        }
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView text;
            protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
            YouTubeThumbnailView youTubeThumbnailView;
            protected ImageView playButton;
            Typeface typeface;
            public MyViewHolder(View itemView) {
                super(itemView);
                typeface=Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-ExtraBold.ttf");
                text=(TextView)itemView.findViewById(R.id.tvVideoTitle);
                text.setTypeface(typeface);
                playButton=(ImageView)itemView.findViewById(R.id.btnYoutube_player);
                //   playButton.setOnClickListener(this);
                relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
                youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);
            }
        }
    }


