package com.workout.exercise.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.workout.exercise.a7minuteworkout.R;
import com.workout.exercise.fragment.fragmentLaunchActivity;
import com.workout.exercise.util.adMobManager;

public class MainActivity extends AppCompatActivity{
    String appPackageName;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appPackageName = getPackageName();
        adMobManager a=new adMobManager();
        a.LoadBannerAdd(MainActivity.this,null);
        context=this;
    }

    public void startExercise(View view) {
        startActivity(new Intent(this, ExerciseActivity.class));
    }

    public void myExercise(View view) {
        Intent i=new Intent(context,fragmentLaunchActivity.class);
        i.putExtra("TAG","myExercise");
        startActivity(i);
    }

    public void rateOnclick(View view) {
        Intent rateIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
        if(rateIntent!=null)
        startActivity(rateIntent);
    }

    public void shareOnClick(View view) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=" + appPackageName);
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    public void moreOnClick(View view) {
       Intent moreIntent=new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/developer?id="+getString(R.string.MORE_APP_LINK)));
       startActivity(moreIntent);
    }

    public void exerciseOnClick(View view) {
      Intent i=new Intent(context,fragmentLaunchActivity.class);
      i.putExtra("TAG","exercise");
      startActivity(i);
    }

    public void calculatorOnClick(View view) {
        Intent i=new Intent(context,fragmentLaunchActivity.class);
        i.putExtra("TAG","bmi");
        startActivity(i);
    }

    public void settingOnClick(View view) {
        Intent i=new Intent(context,fragmentLaunchActivity.class);
        i.putExtra("TAG","settings");
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_Main_activity_title)
                .setMessage(getString(R.string.dialog_main_activity_msg))
                .setPositiveButton(getString(R.string.dialog_rate_us), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent rateIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                        if(rateIntent!=null)
                            startActivity(rateIntent);
                    }
                })
                .setNegativeButton(getString(R.string.dialog_btn_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
                    }
                }).show();

    }
}
