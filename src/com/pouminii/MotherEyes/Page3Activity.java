package com.pouminii.MotherEyes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pouminii.MotherEyes.R;

@SuppressLint("ValidFragment")
public class Page3Activity extends Fragment {
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, 
				ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.activity_tab3, null);

	    	return view;
		}

}