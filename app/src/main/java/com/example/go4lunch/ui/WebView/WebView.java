package com.example.go4lunch.ui.WebView;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.ui.RestaurantDetail.RestaurantDetail;

public class WebView extends AppCompatActivity {
    private android.webkit.WebView webView;
    private Restaurant mRestaurant;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        Intent navigate = getIntent();
        mRestaurant = navigate.getParcelableExtra(RestaurantDetail.KEY_RESTAURANT);
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(mRestaurant.getUrl());

    }
}
