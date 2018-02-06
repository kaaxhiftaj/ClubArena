package com.techease.clubarena.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.techease.clubarena.R;
import com.techease.clubarena.utils.Configuration;

public class BlogDetailFragment extends Fragment {

    WebView webView;
    String url,user_id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_blog_detail, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        user_id=sharedPreferences.getString("user_id","");
        Toast.makeText(getActivity(), user_id, Toast.LENGTH_SHORT).show();
        url=getArguments().getString("blogId");
        webView=(WebView)view.findViewById(R.id.wv);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.setInitialScale(120);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url+"/"+user_id);
                return true;
            }
        });
        webView.loadUrl(url+"/"+user_id);

        return view;
    }

}
