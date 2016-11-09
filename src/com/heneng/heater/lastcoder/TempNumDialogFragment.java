package com.heneng.heater.lastcoder;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.heneng.heater.R;

@SuppressLint("ValidFragment")
public class TempNumDialogFragment extends DialogFragment {	
	
	TextView tempNum;
	public TempNumDialogFragment() {		
		
	}
	public void SetTemp(String temp) {
		if(tempNum!=null)
			tempNum.setText(temp+"℃");
	}
	
	@Override 
	public void onStart() {
		 super.onStart();
		 //控制dialog背景透明度
		 Window window = getDialog().getWindow();
		 WindowManager.LayoutParams windowParams = window.getAttributes();
		 windowParams.dimAmount = 0.00f;
		 windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		 window.setAttributes(windowParams);
	}
    
	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //控制fragment背景透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        
        View view = inflater.inflate(R.layout.frament_tempnum, container);
        tempNum=(TextView)view.findViewById(R.id.fragment_tempnum_txt);
        return view;  
    }  
	

}
