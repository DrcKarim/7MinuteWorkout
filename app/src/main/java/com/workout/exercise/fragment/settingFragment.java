package com.workout.exercise.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.workout.exercise.a7minuteworkout.R;
import com.workout.exercise.broadcast.reminderBroadcast;
import com.workout.exercise.util.adMobManager;
import com.workout.exercise.util.utilhelper;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class settingFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    RadioGroup difficultyGrp;
    Switch soundSwitch,reminderSwitch;
    LinearLayout reminderContainer,weekDays;
    View view;
    Button setReminder;
    private Context context;
    TimePicker reminder_time;

    SharedPreferences sp;
    SharedPreferences.Editor e;

    int hour;
    int min;
    private List<String> listOfDays;
    Set<String> set;
    private InterstitialAd mInterstitialAd;
    adMobManager ad = new adMobManager();

    public settingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.settings, container, false);
        context=getContext();
        initializeView();
        sp= context.getSharedPreferences(utilhelper.FILENAME,Context.MODE_PRIVATE);
        e=sp.edit();

        setViews();

        ad.LoadBannerAdd(getActivity(),view);
        ad.LoadInterstitialAd(getActivity());
        setActionbar();
        return view;
    }

    private void initializeView() {
        difficultyGrp=view.findViewById(R.id.difficulty);
        soundSwitch=view.findViewById(R.id.sound_switch);
        reminderSwitch=view.findViewById(R.id.reminder_switch);
        reminderContainer= view.findViewById(R.id.reminder_container);
        setReminder= view.findViewById(R.id.setReminder);
        reminder_time= view.findViewById(R.id.reminder_time);
        weekDays= view.findViewById(R.id.week_day_group);

        difficultyGrp.setOnCheckedChangeListener(this);
        soundSwitch.setOnCheckedChangeListener(this);
        reminderSwitch.setOnCheckedChangeListener(this);
        setReminder.setOnClickListener(this);

        listOfDays=new ArrayList<>();
        Set<String> set = new HashSet<String>();
    }

    private void setActionbar() {
        utilhelper.setActionbar(R.drawable.settings,getString(R.string.actionbar_setting), this.view,getActivity(),ad);
    }

    private void setViews() {
        setDifficulty(-1,0);
        soundSwitch.setChecked(sp.getBoolean(utilhelper.SOUND,true));
        Boolean reminder=sp.getBoolean(utilhelper.REMINDER,false);
        setReminderSetting(-1,reminder);
    }

    private void setDifficulty(int pos,int exeTime) {
        if(pos==-1) {
            int difficulty = sp.getInt(utilhelper.DIFFICULTY, 1);
            RadioButton rb = (RadioButton) difficultyGrp.getChildAt(difficulty);
            rb.setChecked(true);
        }
        else {
            e.putInt(utilhelper.DIFFICULTY, pos);
            e.putInt(utilhelper.EXETIME,exeTime);
            e.commit();
        }
    }

    private void setReminderSetting(int visibility, boolean b) {
        if(visibility==-1)
        {
            if(b) {
                reminderContainer.setVisibility(View.VISIBLE);
                set=sp.getStringSet("days",null);
                if(set!=null) {
                    for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                        String f = it.next();
                        CheckBox day = (CheckBox) weekDays.getChildAt(Integer.parseInt(f) - 1);
                        day.setChecked(true);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        reminder_time.setHour(sp.getInt("hour",Calendar.getInstance().get(Calendar.HOUR)));
                        reminder_time.setMinute(sp.getInt("min",Calendar.getInstance().get(Calendar.MINUTE)));

                    }
                    else
                    {
                        reminder_time.setCurrentHour(sp.getInt("hour",Calendar.getInstance().get(Calendar.HOUR)));
                        reminder_time.setCurrentMinute(sp.getInt("min",Calendar.getInstance().get(Calendar.MINUTE)));
                    }
                }

            }else{reminderContainer.setVisibility(View.INVISIBLE);};
            reminderSwitch.setChecked(b);
        }
        else {
            reminderContainer.setVisibility(visibility);
        }
    }

    //for switch buttons
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
       int id=compoundButton.getId();
       switch (id)
       {
           case R.id.sound_switch:
               if(b){e.putBoolean(utilhelper.SOUND,true);e.commit();}
               else{e.putBoolean(utilhelper.SOUND,false);e.commit();}
               break;

           case R.id.reminder_switch:
               if(b){setReminderSetting(view.VISIBLE,true);
               }
               else{
                   setReminderSetting(view.INVISIBLE,false);
                   removeReminder();
               }
               break;
       }
    }

    private void removeReminder() {
        for (int i=0;i<weekDays.getChildCount();i++)
        {
            CheckBox day = (CheckBox) weekDays.getChildAt(i);
            day.setChecked(false);
        }
        Intent intent= new Intent(context, reminderBroadcast.class);
        if(set!=null) {
            for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                String f = it.next();
                PendingIntent pd = PendingIntent.getBroadcast(context, Integer.parseInt(f), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                manager.cancel(pd);
                Toast.makeText(context, R.string.toast_reminder_remove, Toast.LENGTH_SHORT).show();

            }
            set.clear();
        }
        e.remove(utilhelper.REMINDER);
        e.remove("days");
        e.commit();
    }

    //for radioGroup
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int pos,exeTime;
        if(radioGroup.getCheckedRadioButtonId()==R.id.one)
        {
            pos=0;
            exeTime=20;
        }
        else if(radioGroup.getCheckedRadioButtonId()==R.id.two)
        {
            pos=1;
            exeTime=30;
        }
        else {
            pos = 2;
            exeTime=40;
        }
        setDifficulty(pos,exeTime);
    }

    @Override
    public void onClick(View view) {
        int flag=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour=reminder_time.getHour();
            min=reminder_time.getMinute();
        }
        else {
            hour = reminder_time.getCurrentHour();
            min = reminder_time.getCurrentMinute();
        }

        Intent intent= new Intent(context, reminderBroadcast.class);
        for(int i=0;i<weekDays.getChildCount();i++) {
            CheckBox day= (CheckBox) weekDays.getChildAt(i);
            if(day.isChecked()==true){
                intent.putExtra("day", i + 1);
                PendingIntent pd = PendingIntent.getBroadcast(context, i + 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                flag = setReminderForWeekDays(pd, i + 1);
            }
        }
        if(flag==1) {
            Set<String> set = new HashSet<String>();
            set.addAll(listOfDays);
            this.set = set;
            e.putBoolean(utilhelper.REMINDER, true);
            e.putStringSet("days", set);
            e.putInt("hour", hour);
            e.putInt("min", min);
            e.commit();
            Toast.makeText(context, R.string.toast_reminder_set, Toast.LENGTH_SHORT).show();
        }
    }

    public int setReminderForWeekDays(PendingIntent pd,int day)
    {
        int i=0;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK,day);
        if(calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_WEEK, 7);
        }
        long differenceTime = calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(manager!=null) {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, differenceTime+System.currentTimeMillis(), AlarmManager.INTERVAL_DAY*7, pd);
            listOfDays.add(String.valueOf(day));
            i=1;
            return i;
        }
       return i;
    }

}
