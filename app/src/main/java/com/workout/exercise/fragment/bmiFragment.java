package com.workout.exercise.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.workout.exercise.a7minuteworkout.R;
import com.workout.exercise.util.adMobManager;
import com.workout.exercise.util.utilhelper;
import com.google.android.gms.ads.InterstitialAd;

public class bmiFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    View view;
    TextView lbl_weight,lbl_height,bmi_result,bmi_result_detail;
    EditText weight,height_cm_feet,height_inch;
    RadioGroup rUnits;
    RadioButton metric,us;
    Button calculate;
    private Context context;
    private InterstitialAd mInterstitialAd;

    adMobManager ad = new adMobManager();

    public bmiFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.bmi_calculator, container, false);
        intializeViews();
        ad.LoadBannerAdd(getActivity(),view);
        ad.LoadInterstitialAd(getActivity());
        setActionbar();
        return view;
    }

    private void setActionbar() {
        utilhelper.setActionbar(R.drawable.bmi,getString(R.string.actionbar_calculate_bmi), view,getActivity(),ad);
    }

    private void intializeViews() {
        lbl_weight=view.findViewById(R.id.lbl_weight);
        lbl_height=view.findViewById(R.id.lbl_height);
        bmi_result=view.findViewById(R.id.bmi_result);
        bmi_result_detail=view.findViewById(R.id.result_details);
        weight=view.findViewById(R.id.bmi_weight);
        height_cm_feet=view.findViewById(R.id.bmi_height_cm);
        height_inch=view.findViewById(R.id.bmi_height_inch);
        rUnits=view.findViewById(R.id.rg_units);
        metric=view.findViewById(R.id.rb_metric_unit);
        us= view.findViewById(R.id.rb_us_unit);
        calculate=view.findViewById(R.id.btn_cal);

        calculate.setOnClickListener(this);
        rUnits.setOnCheckedChangeListener(this);

        context=getContext();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(radioGroup.getCheckedRadioButtonId()==metric.getId())
        {
            lbl_weight.setText(R.string.txt_weight_in_kg);
            lbl_height.setText(R.string.txt_height_in_cm);
            height_cm_feet.setHint("");
            height_inch.setVisibility(View.GONE);
            height_inch.setText("0");
        }
        else if(radioGroup.getCheckedRadioButtonId()==us.getId())
        {
            lbl_weight.setText(R.string.txt_weight_in_kg);
            lbl_height.setText((R.string.txt_height_in_cm));
            height_cm_feet.setHint(R.string.hint_feet);
            height_inch.setVisibility(View.VISIBLE);
            height_inch.setText("");
            height_inch.setHint(R.string.hint_inch);
        }
        weight.setText("");
        height_cm_feet.setText("");
        bmi_result.setText("");
        bmi_result_detail.setText("");
    }

    @Override
    public void onClick(View view) {
        if(!weight.getText().toString().isEmpty() && !height_cm_feet.getText().toString().isEmpty() && !height_inch.getText().toString().isEmpty())
        {
            double w=Double.valueOf(weight.getText().toString());
            double h=Double.valueOf(height_cm_feet.getText().toString());
            if(rUnits.getCheckedRadioButtonId()==metric.getId())
            {
                w*=10000;
            }
            else if(rUnits.getCheckedRadioButtonId()==us.getId())
            {
                w*= 703;
                h =(h*12)+Double.valueOf(height_inch.getText().toString());
            }
            Double bmi=w/(h*h);
            String result[]=getBmiState(bmi);
            bmi= Double.valueOf(String.format("%.2f", bmi));
            bmi_result.setText(getString(R.string.msg_bmi_result)+"\n"+bmi+"\n"+result[0]);
            bmi_result_detail.setText(result[1]);
        }
        else
        {
            Toast.makeText(context, R.string.toast_invalid_values, Toast.LENGTH_SHORT).show();
        }

    }

    private String[] getBmiState(Double bmi) {
        String res[]=new String[2];
        if (bmi<=18.5) {
            res[0] = getString(R.string.msg_under_weight);
            res[1] = getString(R.string.sug_under_weight);
        }
        else if(bmi>18.5 && bmi<=25.0){
            res[0] = getString(R.string.msg_Normal);
            res[1] = getString(R.string.sug_Normal);
        }
        else if(bmi>25 && bmi<=30)
        {
            res[0] = getString(R.string.msg_Over_Weight);
            res[1] = getString(R.string.sug_Over_Weight);
        }
        else if(bmi>30 && bmi<=35) {
            res[0] = getString(R.string.msg_Obese);
            res[1] = getString(R.string.sug_Obese);
        }
        else if(bmi>35 && bmi<=40)
        {
            res[0] = getString(R.string.msg_Severely_Obese);
            res[1] = getString(R.string.sug_Severely_Obese);
        }
        else
        {
            res[0] = getString(R.string.msg_Very_Severely_Obese);
            res[1] = getString(R.string.sug_Very_Severely_Obese);
        }
        return res;
    }
}
