package com.workout.exercise.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.workout.exercise.a7minuteworkout.R;
import com.workout.exercise.adapter.exerciseListAdapter;
import com.workout.exercise.util.adMobManager;
import com.workout.exercise.util.utilhelper;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;
import java.io.InputStream;

public class exerciseListFragment extends Fragment implements exerciseListAdapter.UserClickListener {
    Context context;
    View view = null;
    adMobManager ad = new adMobManager();
    private InterstitialAd mInterstitialAd;

    public exerciseListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String Tag=getTag();
        Activity activity= getActivity();

        context=getContext();

        if(Tag.equals("list"))
        {
            exerciselistFragment(inflater,container,activity);
        }
        else
        {
            exerciseDetailFragment(inflater,container,Tag,activity);
        }

        ad.LoadBannerAdd(getActivity(),view);
        return view;
    }

    private void setActionbar(int img, String title, View view, Activity activity) {
        utilhelper.setActionbar(img,title, this.view,activity, ad);
    }

    private void exerciselistFragment(LayoutInflater inflater, ViewGroup container, Activity activity) {
        RecyclerView rv;
        view= inflater.inflate(R.layout.fragment_exercise_list, container, false);
        ad.LoadInterstitialAd(getActivity());
        setActionbar(R.drawable.exercises,getString(R.string.actionbar_exercise_list),view,activity);

        String[] elist=context.getResources().getStringArray(R.array.exe_names);
        String[] imglist=context.getResources().getStringArray(R.array.images);
        rv=view.findViewById(R.id.exe_list);
        rv.setLayoutManager(new LinearLayoutManager(context));
        setRecyclerView(elist,imglist,rv);
    }

    private void exerciseDetailFragment(LayoutInflater inflater, ViewGroup container, String Tag, Activity activity) {
        view= inflater.inflate(R.layout.exercise_details, container, false);

        String[] elist=context.getResources().getStringArray(R.array.exe_names);
        String[] imglist=context.getResources().getStringArray(R.array.images);

        ad.LoadInterstitialAd(getActivity());

        setActionbar(-1,elist[Integer.parseInt(Tag)],view,activity);
        ImageView img=view.findViewById(R.id.exedetail_header);
        TextView tv=view.findViewById(R.id.exedetail_text);
        String[] des=context.getResources().getStringArray(R.array.exe_details);
        tv.setText(des[Integer.parseInt(Tag)]);
        Log.e("tag",Tag);
        setimg(img,Tag);
    }

    private void setimg(ImageView img,String tag) {
        String[] imglist=context.getResources().getStringArray(R.array.images);
        AssetManager manager = context.getAssets();
        InputStream open = null;
        try {
            open = manager.open(imglist[Integer.parseInt(tag)]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(open);
        img.setImageBitmap(bitmap);
    }

    private void setRecyclerView(String[] elist, String[] imglist, RecyclerView rv) {
        rv.setAdapter(new exerciseListAdapter(elist,imglist,this,context));
    }

    @Override
    public void onUserClick(int pos) {
        Fragment f=new exerciseListFragment();
        getFragmentManager().beginTransaction().replace(R.id.frm_layout,f, String.valueOf(pos)).addToBackStack(getClass().getSimpleName()).commit();
    }
}
