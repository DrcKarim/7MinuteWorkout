package com.workout.exercise.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.workout.exercise.a7minuteworkout.R;
import com.workout.exercise.util.adMobManager;
import com.google.android.gms.ads.InterstitialAd;

public class fragmentLaunchActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    adMobManager ad = new adMobManager();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_launch);
        ad.LoadInterstitialAd(fragmentLaunchActivity.this);
        context=this;
        Intent i=getIntent();
        Fragment f = null;
        String activityName=i.getStringExtra("TAG");
        if(activityName.equals("exercise")){
           f = new exerciseListFragment();
        }
        else if(activityName.equals("bmi"))
        {
            f = new bmiFragment();
        }
        else if(activityName.equals("settings"))
        {
            f=new settingFragment();
        }
        else if(activityName.equals("myExercise"))
        {
            f=new myExerciseFragment();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.frm_layout,f,"list").commit();
    }

    @Override
    public void onBackPressed() {
        ad.onbackPress(fragmentLaunchActivity.this);
        super.onBackPressed();
    }
}

