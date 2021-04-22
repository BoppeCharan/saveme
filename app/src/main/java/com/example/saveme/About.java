package com.example.saveme;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class About extends AppCompatActivity {
    private AdView madview;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_about);

        MobileAds.initialize(this,"ca-app-pub-3908726115058703~9607802297");
        madview=(AdView)findViewById(R.id.adview2);
        AdRequest adRequest= new AdRequest.Builder().build();
        madview.loadAd(adRequest);

        MobileAds.initialize(this,
                "ca-app-pub-3908726115058703~9607802297");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3908726115058703/6900875763");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }
    @Override
    public void onBackPressed() {
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();

            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {

                    super.onAdClosed();
                    finish();
                }
            });

        }
        else {
            super.onBackPressed();
        }
    }
}