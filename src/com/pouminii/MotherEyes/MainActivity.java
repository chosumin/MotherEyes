package com.pouminii.MotherEyes;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pouminii.MotherEyes.R;

/* 수정된 Main Activity */
public class MainActivity extends FragmentActivity implements OnClickListener {
	
	private int NUM_PAGES = 3;		// 최대 페이지의 수 
	
	/* Fragment numbering */
	public final static int FRAGMENT_PAGE1 = 0;
	public final static int FRAGMENT_PAGE2 = 1;
	public final static int FRAGMENT_PAGE3 = 2;
	Boolean is_finish = false;
	private StaticData mData= StaticData.GetInstance();
	private AutoLayout autoLayout= AutoLayout.GetInstance();
	

	ViewPager mViewPager;			// View pager를 지칭할 변수 
	
	Button page1Btn, page2Btn, page3Btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mData.disWidth= this.getResources().getDisplayMetrics().widthPixels;
		mData.disHeight= this.getResources().getDisplayMetrics().heightPixels;
		autoLayout.init(this, mData.disWidth, mData.disHeight);
		setContentView(R.layout.activity_main);
		RelativeLayout mainLayout= (RelativeLayout) findViewById(R.id.mainact); // 최상위레이아웃을가져옴
		autoLayout.setView(mainLayout);
		startActivity(new Intent(this,Splash.class));
		
		// ViewPager를 검색하고 Adapter를 달아주고, 첫 페이지를 선정해준다.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
		mViewPager.setCurrentItem(FRAGMENT_PAGE1);
		
		page1Btn = (Button) findViewById(R.id.Page1Btn);
		page1Btn.setOnClickListener(this);
		page2Btn = (Button) findViewById(R.id.Page2Btn);
		page2Btn.setOnClickListener(this);
		page3Btn = (Button) findViewById(R.id.Page3Btn);
		page3Btn.setOnClickListener(this);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				page1Btn.setSelected(false);
				page2Btn.setSelected(false);
				page3Btn.setSelected(false);
				
				switch(position){
					case 0:
						page1Btn.setSelected(true);
						break;
					case 1:
						page2Btn.setSelected(true);
						break;
					case 2:
						page3Btn.setSelected(true);
						break;
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		page1Btn.setSelected(true);
	}
	
	// FragmentPageAdater : Fragment로써 각각의 페이지를 어떻게 보여줄지 정의한다. 
	private class pagerAdapter extends FragmentPagerAdapter{

		public pagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}

		// 특정 위치에 있는 Fragment를 반환해준다.
		@Override
		public Fragment getItem(int position) {
			
			switch(position){
				case 0:
					return new Page1Activity();
				case 1:
					return new Page2Activity();
				case 2:
					return new Page3Activity();
				default:
					return null;
			}
		}
		
		// 생성 가능한 페이지 개수를 반환해준다.
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return NUM_PAGES;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		int id = v.getId();
		if (id == R.id.Page1Btn) {
			mViewPager.setCurrentItem(FRAGMENT_PAGE1);
		} else if (id == R.id.Page2Btn) {
			mViewPager.setCurrentItem(FRAGMENT_PAGE2);
		} else if (id == R.id.Page3Btn) {
			mViewPager.setCurrentItem(FRAGMENT_PAGE3);
		}
	}	
	@Override

    public void onBackPressed(){
			//메인 종료되기전 한번 물어보고 종료
			if(is_finish)							
				finish();
			else {
				Toast.makeText(getBaseContext(),	"종료하시려면 이전버튼을 한번 더 누르세요.", 0).show();
				is_finish = true; //종료한다.
			}	
    }



}
