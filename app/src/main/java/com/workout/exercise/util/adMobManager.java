package com.workout.exercise.util;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.workout.exercise.a7minuteworkout.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class adMobManager {

    InterstitialAd mInterstitialAd;
    int counter=0;
    public void LoadBannerAdd(Activity activity,View view)
    {
        AdView adView;
        if(view!=null)
        {
           adView =view.findViewById(R.id.adView);
        }else {
           adView = activity.findViewById(R.id.adView);
        }
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void LoadInterstitialAd(Activity activity) {
        mInterstitialAd = new InterstitialAd(activity);
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        mInterstitialAd.setAdUnitId(activity.getString(R.string.admob_fullpage_ad_id));
        mInterstitialAd.loadAd(adRequestBuilder.build());
//        mInterstitialAd.setAdListener((new AdListener() {
//            public void onAdClosed() {
//                super.onAdClosed();
//                if (AdManager.INSTANCE != null) {
//                    loadAd();
//                }
//            }
//        }));
    }

    public void showInterstitialAd()
    {
        this.mInterstitialAd.show();
    }

    public void onbackPress(Activity activity)
    {
        int counter=utilhelper.getAdCounter(activity);
        if(counter==4)
        {
            showInterstitialAd();
            counter=1;
            utilhelper.addAdCounter(activity,counter);
        }
        else
        {
            counter++;
            utilhelper.addAdCounter(activity,counter);
        }
        Log.e("Counter", String.valueOf(counter));
    }
    public void onbackPressf(Activity activity)
    {
        int counter=utilhelper.getAdCounter(activity);
        if(counter==4)
        {
            showInterstitialAd();
            counter=1;
            utilhelper.addAdCounter(activity,counter);
        }
        else
        {
            counter++;
            utilhelper.addAdCounter(activity,counter);
        }
        Log.e("Counter", String.valueOf(counter));
    }

}
