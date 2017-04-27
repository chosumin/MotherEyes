package com.pouminii.MotherEyes;
import java.util.ArrayList;

import com.pouminii.MotherEyes.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
 
public class Wait extends Activity {
	ArrayList<String> items;
	ArrayAdapter<String> adapter;
	ListView listView;
	private StaticData  mData= StaticData.GetInstance();
	private AutoLayout autoLayout= AutoLayout.GetInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        mData.disWidth= this.getResources().getDisplayMetrics().widthPixels;
		mData.disHeight= this.getResources().getDisplayMetrics().heightPixels;
		autoLayout.init(this, mData.disWidth, mData.disHeight);
        setContentView(R.layout.activity_wait);
        RelativeLayout mainLayout= (RelativeLayout) findViewById(R.id.wai); // 최상위레이아웃을가져옴
		autoLayout.setView(mainLayout);
    	items = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, items);
		listView = (ListView)findViewById(R.id.list);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(mItemClickListener);
		findViewById(R.id.add).setOnClickListener(mClickListener);
		findViewById(R.id.button1).setOnClickListener(mClickListener);
		SharedPreferences prefs = getSharedPreferences("PrefName", Context.MODE_PRIVATE);
		int size = prefs.getInt("Status_size", 0);  
		for(int i=0;i<size;i++) 
		{
			items.add(prefs.getString("Status_" + i, null));  
		}
    }
	Button.OnClickListener mClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.add) {
				AlertDialog.Builder alert = new AlertDialog.Builder(Wait.this);
				alert.setTitle("계획표");
				// Set an EditText view to get user input
				final EditText input = new EditText(Wait.this);
				alert.setView(input);
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = input.getText().toString();
						value.toString();
						String text = input.getText().toString();
						if(text.length() != 0) {
							items.add(text);
							input.setText("");
							adapter.notifyDataSetChanged();
						}
					}
				});
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						// Canceled.

					}
				});
				alert.show();
			}
			if(id == R.id.button1){
				finish();
			}
		}
	};


	// ListView에서 사용하는 listener
	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView parent, View view, final int position, long id) {
			AlertDialog.Builder alert = new AlertDialog.Builder(Wait.this);
			alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {

					int id = listView.getCheckedItemPosition();
					if(id != ListView.INVALID_POSITION) {
						items.remove(id);
						listView.clearChoices();
						adapter.notifyDataSetChanged();
					}
				}
			});
			alert.setNegativeButton("공부 완료!", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int whichButton) {
					int id = listView.getCheckedItemPosition();
					String plus = listView.getItemAtPosition(position).toString(); 
					plus += " - 완료!";
					items.add(plus);
					items.remove(id);
					listView.clearChoices();
					adapter.notifyDataSetChanged();
				}
			});
			alert.show(); 
		}
	};
	public void onPause(){
		super.onPause();
		SharedPreferences sp= getSharedPreferences("PrefName", Context.MODE_PRIVATE);
		SharedPreferences.Editor mEdit1= sp.edit();
		mEdit1.putInt("Status_size",items.size()); /*sKey is an array*/ 
		for(int i=0;i<items.size();i++)  
		{
			mEdit1.remove("Status_" + i);
			mEdit1.putString("Status_" + i, items.get(i));  
		}
		mEdit1.commit();
	}
}
 