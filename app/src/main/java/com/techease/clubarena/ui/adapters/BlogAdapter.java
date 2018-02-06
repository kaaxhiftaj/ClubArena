package com.techease.clubarena.ui.adapters;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techease.clubarena.R;
import com.techease.clubarena.models.BlogModel;
import com.techease.clubarena.ui.fragments.BlogDetailFragment;

import java.util.ArrayList;

/**
 * Created by Adam Noor on 05-Feb-18.
 */

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.MyViewHolder> {

    ArrayList<BlogModel> blogModelArrayList;
    Context context;
    String strBlogId;
    public BlogAdapter(Context context, ArrayList<BlogModel> blogModels) {
        this.context=context;
        this.blogModelArrayList=blogModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cutom_blog, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final BlogModel model=blogModelArrayList.get(position);
       holder.textViewTitle.setText(model.getBlogTitle());
        Glide.with(context).load(model.getBlogImageLink()).into(holder.imageView);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strBlogId=model.getBlogLink();
                Bundle bundle=new Bundle();
                bundle.putString("blogId",strBlogId);
                Fragment fragment=new BlogDetailFragment();
                fragment.setArguments(bundle);
                ((AppCompatActivity)context).getFragmentManager().beginTransaction().replace(R.id.fragment_main , fragment).addToBackStack("abc").commit();
            }
        });
    }

    @Override
    public int getItemCount() {

        return blogModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView,shareBtn;
        TextView textViewTitle;
        LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewTitle=(TextView)itemView.findViewById(R.id.tvBlogTitle);
          imageView=(ImageView)itemView.findViewById(R.id.ivBlogImage);
           shareBtn=(ImageView)itemView.findViewById(R.id.sharebtnNews);
           linearLayout=(LinearLayout)itemView.findViewById(R.id.llParent);
        }
    }
}

