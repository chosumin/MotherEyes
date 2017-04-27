package com.pouminii.MotherEyes;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;
import com.kakao.KakaoLink;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.pouminii.MotherEyes.R;
import com.pouminii.MotherEyes.R.drawable;



import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Studying extends Activity 
{
	Boolean is_finish = false;
	SurfaceView sv_viewFinder;
	SurfaceHolder sh_viewFinder;
	Camera camera;
	Button btn_shutter;
	ImageView iv_preview;
	boolean inProgress = false;
	TextView mEllapse;
	TextView mSplit;
	Button mBtnStart;
	Button mBtnEnd;
	final static int IDLE = 0;
	final static int RUNNING = 1;
	final static int PAUSE = 2;
	int mStatus = IDLE;
	long mBaseTime;
	long mPauseTime;
	int mSplitCount;
	IntentFilter filter = new IntentFilter();
	private StaticData  mData= StaticData.GetInstance();
	private AutoLayout autoLayout= AutoLayout.GetInstance();
	private WifiManager wifiManager;
	private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mData.disWidth= this.getResources().getDisplayMetrics().widthPixels;
		mData.disHeight= this.getResources().getDisplayMetrics().heightPixels;
		autoLayout.init(this, mData.disWidth, mData.disHeight);
		setContentView(R.layout.studying);	
		RelativeLayout mainLayout= (RelativeLayout) findViewById(R.id.stud); // 理쒖긽�?���젅�씠�븘�썐�쓣媛��졇�샂
		autoLayout.setView(mainLayout);
		wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		if(wifiManager.isWifiEnabled()){
			wifiManager.setWifiEnabled(false);
		}
		try{
			final ConnectivityManager conman = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			final Class conmanClass = Class.forName(conman.getClass().getName());
			final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
			connectivityManagerField.setAccessible(true);
			final Object connectivityManager = connectivityManagerField.get(conman);
			final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
			final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);

			setMobileDataEnabledMethod.invoke(connectivityManager, false); //�뿬湲곗�? true, false濡� on/off

		}catch(Exception e){
			e.getStackTrace();
		}

		Toast toast = Toast.makeText(this, "Wifi,?��?��?�� ?��?��?��?���? 차단?��?��?��", 

				Toast.LENGTH_SHORT); 

		toast.show(); 
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		sv_viewFinder = (SurfaceView) findViewById(R.id.sv_viewFinder);
		sh_viewFinder = sv_viewFinder.getHolder();
		sh_viewFinder.addCallback(surfaceListener);
		sh_viewFinder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mEllapse = (TextView)findViewById(R.id.ellapse);
		mBtnStart = (Button)findViewById(R.id.btnstart);
		mBtnEnd = (Button)findViewById(R.id.btnend);
		// setListener
		Timer timer = new Timer();

		TimerTask tt = new TimerTask() 
		{
			@Override
			public void run() 
			{
				startTakePicture ();
			}
		};
		timer.schedule(tt, 900000);
		TimerTask ttt = new TimerTask() 
		{
			@Override
			public void run() 
			{
				startTakePicture ();
			}
		};
		timer.schedule(ttt, 1800000);
		TimerTask tttt = new TimerTask() 
		{
			@Override
			public void run() 
			{
				startTakePicture ();
			}
		};
		timer.schedule(tttt, 2700000);
		TimerTask ttttt = new TimerTask() 
		{
			@Override
			public void run() 
			{
				startTakePicture ();
			}
		};
		timer.schedule(ttttt, 3600000);
		TimerTask tttttt = new TimerTask() 
		{
			@Override
			public void run() 
			{
				startTakePicture ();
			}
		};
		timer.schedule(tttttt, 4500000);
		TimerTask ttttttt = new TimerTask() 
		{
			@Override
			public void run() 
			{
				startTakePicture ();
			}
		};
		timer.schedule(ttttttt, 5400000);
		TimerTask t1 = new TimerTask() 
		{
			@Override
			public void run() 
			{
				startTakePicture ();
			}
		};
		timer.schedule(t1, 6300000);
		TimerTask t2 = new TimerTask() 
		{
			@Override
			public void run() 
			{
				startTakePicture ();
			}
		};
		timer.schedule(t2, 7200000);

	}


	public void onDestroy(){
		mTimer.removeMessages(0);
		try{
			final ConnectivityManager conman = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			final Class conmanClass = Class.forName(conman.getClass().getName());
			final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
			connectivityManagerField.setAccessible(true);
			final Object connectivityManager = connectivityManagerField.get(conman);
			final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
			final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);

			setMobileDataEnabledMethod.invoke(connectivityManager, true); //�뿬湲곗�? true, false濡� on/off

		}catch(Exception e){
			e.getStackTrace();
		}
		wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		if(wifiManager.isWifiEnabled()==false){
			wifiManager.setWifiEnabled(true);
		}
		Toast toast = Toast.makeText(this, "Wifi,?��?��?�� ?��?��?��?���? 구동 ?��?��?��.", 

				Toast.LENGTH_SHORT); 
		toast.show(); 
		super.onDestroy();
	}

	public SurfaceHolder.Callback surfaceListener = new SurfaceHolder.Callback() 
	{
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
		{
			Log.i("1", "sufraceListener   ");
			Camera.Parameters parameters = camera.getParameters();

			parameters.setPreviewSize(width, height);

			camera.startPreview();
		}
		@Override
		public void surfaceCreated(SurfaceHolder holder) 
		{
			Log.i("1", "sufraceListener");
			int int_cameraID = 0;
			/* 移�?찓�?��媛� �뿬�윭媛� �씪 寃쎌?�� 洹� �닔?���? 媛��졇�샂  */
			int numberOfCameras = Camera.getNumberOfCameras();
			CameraInfo cameraInfo = new CameraInfo();
			for(int i=0; i < numberOfCameras; i++) 
			{
				Camera.getCameraInfo(i, cameraInfo);

				if(cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT)

					int_cameraID = i;
			}
			camera = Camera.open(int_cameraID);
			try 
			{
				camera.setPreviewDisplay(sh_viewFinder);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) 
		{
			Log.i("1", "sufraceListener ");
			camera.release();
			camera = null;
		}
	};
	public void startTakePicture ()
	{
		if (camera != null && inProgress == false)
		{
			camera.takePicture(null, null, takePicture);
			inProgress = true;
		}
	}

	public Camera.PictureCallback takePicture = new Camera.PictureCallback() 
	{    
		@Override
		public void onPictureTaken(byte[] data, Camera camera) 
		{
			Log.d("1", "=== takePicture ===");
			if (data != null)
			{
				Log.v("1", "takePicture JPEG ");
				Bitmap bitmap = BitmapFactory.decodeByteArray(data,  0,  data.length);
				String imageSaveUri = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"?���? ???���?", "???��?��?��?��?��?��.");
				Uri uri = Uri.parse(imageSaveUri);
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));
				camera.startPreview();
				inProgress = false;
			}
			else
			{
				Log.e("1", "takePicture data null");
			}
		}
	};
	public void mOnClick(View v){
		int id = v.getId();
		if (id == R.id.button1) {
			Intent intent1 = new Intent(this,Wait.class);
			startActivity(intent1);
		} else if (id == R.id.btnstart) {
			switch(mStatus){
			case IDLE :
				mBaseTime = SystemClock.elapsedRealtime();
				mTimer.sendEmptyMessage(0);
				mStatus = RUNNING;
				break;
			case RUNNING :
				mTimer.removeMessages(0);
				mPauseTime = SystemClock.elapsedRealtime();
				mBtnEnd.setBackgroundResource(drawable.done);
				mBtnEnd.setVisibility(View.VISIBLE);
				mBtnStart.setBackgroundResource(drawable.pp);
				mStatus = PAUSE;
				break;
			case PAUSE:

				long now = SystemClock.elapsedRealtime();
				mBaseTime += (now - mPauseTime);
				mTimer.sendEmptyMessage(0);
				mBtnStart.setBackgroundResource(drawable.st);
				mBtnEnd.setVisibility(View.INVISIBLE);
				mStatus = RUNNING;
				break;
			}
		} else if (id == R.id.btnend) {
			switch(mStatus){
			case PAUSE:
				Page1Activity.AActivity.finish();
				Intent intent = new Intent(this,MainActivity.class);
				intent.putExtra("sSplit", mEllapse.getText().toString());
				startActivity(intent);
				finish();
				break;
			}
		}
	}
	Handler mTimer = new Handler(){
		public void handleMessage(Message msg){		
			mEllapse.setText(getEllapse());
			mTimer.sendEmptyMessage(0);
		}
	};
	String getEllapse(){
		long now = SystemClock.elapsedRealtime();
		long ell = now - mBaseTime;
		String sEll = String.format("%02d:%02d:%02d", ell/3600000,ell /1000/60, (ell/1000)%60);
		return sEll;
	}
	 public void onBackPressed(){
			//硫붿?�� ?��?��즺�릺湲곗쟾 �븳踰� ?��?��뼱蹂?��?? ?��?���?
			if(is_finish)							
				finish();
			else {
				new AlertDialog.Builder(this)
				.setMessage("초기?��면으�? ?��?���??��경우 ?��?�� 측정?��?�� ?��간이 초기?��?��?��?��. ?��간을 ???��?��채로 계획?���? ?��?��?��?��?���? ?��쪽하?��?�� ?��버튼?�� ?��?��주세?��.")
				.setPositiveButton("초기?��면으�?", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int whichButton){
						finish();
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int whichButton){
					}
				})
				.show();
				//?��?��즺�븳�?��.
			}	
}
}



