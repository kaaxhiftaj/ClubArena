package com.techease.clubarena.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techease.clubarena.R;
import com.techease.clubarena.models.clubInfoPersonModel;

import java.util.List;

/**
 * Created by Adam Noor on 05-Feb-18.
 */

public class clubInfoPersonAdapter extends RecyclerView.Adapter<clubInfoPersonAdapter.MyViewHolder> {
    Context context;
    List<clubInfoPersonModel> models;

    public clubInfoPersonAdapter(Context context, List<clubInfoPersonModel> cluebInfoPersonModels) {
        this.context=context;
        this.models=cluebInfoPersonModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_infolist, parent, false);

        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final clubInfoPersonModel model=models.get(position);
        holder.tvTitle.setText(model.getTitle());
        holder.tvDesignation.setText(model.getDesignation());

    }


    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvDesignation;
        Typeface typeface;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvTitle=(TextView)itemView.findViewById(R.id.tvTitle);
            tvDesignation=(TextView)itemView.findViewById(R.id.tvDesignation);
        }
    }
}
