package com.techease.clubarena.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.techease.clubarena.R;
import com.techease.clubarena.models.ModelClubDetails;
import com.techease.clubarena.ui.adapters.ClubDetailsPhotoAdapter;
import com.techease.clubarena.utils.InternetUtils;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ClubDetailPhotos extends Fragment {

    ArrayList<ModelClubDetails> images_model_list;
    ClubDetailsPhotoAdapter images_adapter;
    Unbinder unbinder;

    @BindView(R.id.club_detail_photo)
    GridView gridView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_club_detail_photos, container, false);
        unbinder = ButterKnife.bind(this, v);

        if(InternetUtils.isNetworkConnected(getActivity()))
        {


            images_model_list = new ArrayList<>();
            images_adapter = new ClubDetailsPhotoAdapter(getActivity(), images_model_list);
            gridView.setAdapter(images_adapter);
            Toast.makeText(getActivity(), String.valueOf(images_model_list), Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }
        return  v ;
    }

}
