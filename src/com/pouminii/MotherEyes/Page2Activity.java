package com.pouminii.MotherEyes;

import java.util.ArrayList;
import java.util.Collections;

import com.pouminii.MotherEyes.R;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent; 

public class Page2Activity extends Fragment {
	ArrayList<String> itemss;
	ArrayAdapter<String> adapter;
	ListView listView;
	private StaticData mData= StaticData.GetInstance();
	private AutoLayout autoLayout= AutoLayout.GetInstance();
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_tab2, null);
		mData.disWidth= getActivity().getResources().getDisplayMetrics().widthPixels;
		mData.disHeight= getActivity().getResources().getDisplayMetrics().heightPixels;
		autoLayout.init(getActivity(), mData.disWidth, mData.disHeight);
		RelativeLayout mainLayout= (RelativeLayout) view.findViewById(R.id.ta2); // 理쒖긽�?���젅�씠�븘�썐�쓣媛��졇�샂
		autoLayout.setView(mainLayout);
		itemss = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, itemss);
		listView = (ListView)view.findViewById(R.id.list);
		listView.setAdapter(adapter);
		Collections.reverse(itemss);
		Intent intent = getActivity().getIntent();
		String text1 = intent.getStringExtra("sSplit");
		if(text1 != null){
		itemss.add(text1);
		adapter.notifyDataSetChanged();
		}
		SharedPreferences prefss = this.getActivity().getSharedPreferences("PrefNames4", Context.MODE_PRIVATE);
		int size = prefss.getInt("Status_size", 0);  
		for(int i=0;i<size;i++) 
		{
			itemss.add(prefss.getString("Status_" + i, null));  
		}
		

		return view;
	}
	public void onPause(){
		super.onPause();
		SharedPreferences sps= this.getActivity().getSharedPreferences("PrefNames4", Context.MODE_PRIVATE);
		SharedPreferences.Editor mEdit1= sps.edit();
		mEdit1.putInt("Status_size",itemss.size()); /*sKey is an array*/ 
		for(int i=0;i<itemss.size();i++)  
		{
			mEdit1.remove("Status_" + i);
			mEdit1.putString("Status_" + i, itemss.get(i));  
		}
		mEdit1.commit();
	}
}