package com.task.omnify.appomnify.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.task.omnify.appomnify.R;

public class WebViewFragment extends Fragment {
    String url;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_webview, container, false);

        WebView webView =  v.findViewById(R.id.webview);
        webView.loadUrl(url);
        return v;
    }

    public static WebViewFragment getInstance(String url){
        WebViewFragment webViewFragment= new WebViewFragment();
        webViewFragment.url=url;
        return webViewFragment;
    }
}
