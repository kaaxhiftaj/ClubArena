package com.techease.clubarena.ui.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.techease.clubarena.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AuthChoice extends Fragment  {

@BindView(R.id.btn_sign_in)
    Button btn_sign_in ;

@BindView(R.id.btn_register)
Button btn_register;
Unbinder unbinder ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_auth_choice_screen, container, false);
        unbinder = ButterKnife.bind(this, v);



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new RegisterFragment();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack("tag").commit();

            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new LoginFragment();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack("tag").commit();

            }
        });

        return v ;
    }

}
