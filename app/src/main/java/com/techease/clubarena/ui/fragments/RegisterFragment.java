package com.techease.clubarena.ui.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.techease.clubarena.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RegisterFragment extends Fragment {


    @BindView(R.id.et_username_signup)
    EditText et_username_signnup;

    @BindView(R.id.et_email_signup)
    EditText et_email_signup;

    @BindView(R.id.et_password_signup)
    EditText et_password_signup ;

    @BindView(R.id.btn_signup)
    Button btn_signup;

    @BindView(R.id.tv_signin)
    TextView tv_signin;

    Unbinder unbinder ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, v);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Raleway-ExtraBold.ttf");
        tv_signin.setTypeface(custom_font);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new LoginFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack("tag").commit();
            }
        });

        return v;
    }

}
