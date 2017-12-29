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

import com.techease.clubarena.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GetLocation extends Fragment {

    @BindView(R.id.use_my_location)
    Button btn_use_my_location ;

    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_get_location, container, false);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Raleway-ExtraBold.ttf");
     //   btn_use_my_location.setTypeface(custom_font);

        unbinder = ButterKnife.bind(this, v);

        return v;
    }

}
