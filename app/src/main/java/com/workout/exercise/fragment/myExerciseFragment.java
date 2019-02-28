package com.workout.exercise.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.workout.exercise.a7minuteworkout.R;
import com.workout.exercise.sqlite.MyDatabase;
import com.workout.exercise.util.adMobManager;
import com.workout.exercise.util.utilhelper;
import com.google.android.gms.ads.InterstitialAd;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;

public class myExerciseFragment extends Fragment {

    View view;
    MaterialCalendarView calender;
    private Context context;
    private InterstitialAd mInterstitialAd;
    adMobManager ad = new adMobManager();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_my_exercise, container, false);
        context=getContext();
        intializeViews();

        ad.LoadBannerAdd(getActivity(),view);
        ad.LoadInterstitialAd(getActivity());
        setActionbar(R.drawable.history,getString(R.string.actionbar_myExerciseFragment),view,getActivity());
        return view;
    }

    private void intializeViews() {
        calender= view.findViewById(R.id.calendar);
        calender.setSelectedDate(CalendarDay.today());
        calender.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        MyDatabase mydb=new MyDatabase(context);
        final ArrayList<CalendarDay> dates = mydb.getDate();
        calender.addDecorator(new DayViewDecorator() {

            @Override
            public boolean shouldDecorate(CalendarDay day) {
               return  dates.contains(day);
            }

            @Override
            public void decorate(DayViewFacade view) {
                 view.addSpan(new DotSpan(5f, Color.RED));
            }
        });
    }

    private void setActionbar(int img, String title, View view, Activity activity) {
        utilhelper.setActionbar(img,title, view,activity,ad);
    }

}
