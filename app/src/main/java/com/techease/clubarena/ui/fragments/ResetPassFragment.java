package com.techease.clubarena.ui.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.techease.clubarena.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ResetPassFragment extends Fragment {

    @BindView(R.id.et_pass_reset)
    EditText et_pass_reset;

    @BindView(R.id.et_passmatch_reset)
    EditText et_passmatch_reset ;

    @BindView(R.id.btn_reset)
    Button btn_reset ;

    Unbinder unbinder ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reset_pass, container, false);
        unbinder = ButterKnife.bind(this, v);

        btn_reset.setOnClickListener(new View.OnClickListener() {
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
