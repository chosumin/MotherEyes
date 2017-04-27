package com.pouminii.MotherEyes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.kakao.AppActionBuilder;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.pouminii.MotherEyes.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class Page1Activity extends Fragment {

	private StaticData mData= StaticData.GetInstance();
	private AutoLayout autoLayout= AutoLayout.GetInstance();
	private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    public static Activity AActivity;
	private static String TAG = "MainActivity";
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_IMAGE_ALBUM = 2;
	private static final int REQUEST_IMAGE_CROP = 3;
	private ImageView mImageView;
	String mCurrentPhotoPath;
	private Uri contentUri;
	ArrayList<String> items;
	ArrayAdapter<String> adapter;
	ListView listView;
	String Uri11;
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_tab1, container,false);
		mData.disWidth= getActivity().getResources().getDisplayMetrics().widthPixels;
		mData.disHeight= getActivity().getResources().getDisplayMetrics().heightPixels;
		autoLayout.init(getActivity(), mData.disWidth, mData.disHeight);
		RelativeLayout mainLayout= (RelativeLayout) view.findViewById(R.id.ta1); // 최상위레이아웃을가져옴
		autoLayout.setView(mainLayout);
		AActivity= this.getActivity();
		 final String stringText = "카카오링크 테스트 텍스트";
		
		  try {
			kakaoLink = KakaoLink.getKakaoLink(getActivity());
			 kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
		} catch (KakaoParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		items = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, items);
		listView = (ListView)view.findViewById(R.id.list);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // 하나의 항목만 선택 가능
		// 위 Adapter의 레이아웃이 single이기 때문에 listview의 선택모드도 CHOICE_MODE_SINGLE이여야 한다.

		listView.setOnItemClickListener(mItemClickListener);
		view.findViewById(R.id.add).setOnClickListener(mClickListener);
		
		mCurrentPhotoPath = null;		
		mImageView = (ImageView)view.findViewById(R.id.mImageView);
		File path = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);		

		if(!path.exists()) {
			path.mkdirs();	
		}
		mImageView.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0){
				Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, REQUEST_IMAGE_ALBUM);	
			}
		});
		Button start = (Button)view.findViewById(R.id.button1);
		start.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(getActivity(), Studying.class);
				startActivity(intent);
		
			}
		});
		Intent intent = getActivity().getIntent();
		final String text1 = intent.getStringExtra("sSplit");
		Button send = (Button)view.findViewById(R.id.button2);
		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(adapter != null && text1 != null){
					try {	
						String send11 ="";
						for(int i=0;i<items.size();i++)  
						{
							send11 +=listView.getItemAtPosition(i).toString() + "\n"; 
						}

						kakaoTalkLinkMessageBuilder.addText("오늘의 공부시간 - " + text1 + "\n"+"오늘의 계획 -\n"+send11);                     
						kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(),getActivity());
					} catch (KakaoParameterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					new AlertDialog.Builder(getActivity())
					.setMessage("계획표 작성과 오늘의 공부시간을 기록하신 뒤 눌러주세요")
					.setPositiveButton("확인", new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int whichButton){
						}
					})
					.show();

				}}
		});
		SharedPreferences prefs = this.getActivity().getSharedPreferences("PrefName", Context.MODE_PRIVATE);
		int size = prefs.getInt("Status_size", 0);  
		for(int i=0;i<size;i++) 
		{
			items.add(prefs.getString("Status_" + i, null));  
		}
		return view;
	}

	Button.OnClickListener mClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.add) {
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				
				alert.setTitle("계획표");
				// Set an EditText view to get user input
				final EditText input = new EditText(getActivity());
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
		}
	};


	// ListView에서 사용하는 listener
	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView parent, View view, final int position, long id) {
			AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.camera) {
			dispatchTakePictureIntent();
			return true;
		} else if (itemId == R.id.album) {
			Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, REQUEST_IMAGE_ALBUM);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}



	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
				//            ...
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				contentUri = Uri.fromFile(photoFile);        	
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(resultCode == getActivity().RESULT_OK) {
			switch(requestCode) {
			case REQUEST_IMAGE_ALBUM:
				contentUri = data.getData();
				
			case REQUEST_IMAGE_CAPTURE:
				rotatePhoto();					
				cropImage(contentUri);
			
				break;
			case REQUEST_IMAGE_CROP:
				Bundle extras = data.getExtras();
				if(extras != null) {
					Bitmap bitmap = (Bitmap)extras.get("data");						
					mImageView.setImageBitmap(bitmap);
					
					//임시로 저장한 파일 삭제.
					if(mCurrentPhotoPath != null) {
						File f = new File(mCurrentPhotoPath);
						if(f.exists()) {
							f.delete();
						}
						mCurrentPhotoPath = null;
					}
				}
				break;
			}
		}	    
	}	

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
				);

		mCurrentPhotoPath = image.getAbsolutePath(); //나중에 Rotate하기 위한 파일 경로.
		
		return image;	
	}	

	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		getActivity().sendBroadcast(mediaScanIntent);
	}

	private void cropImage(Uri contentUri) {
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		//indicate image type and Uri of image
		cropIntent.setDataAndType(contentUri, "image/*");
		//set crop properties
		cropIntent.putExtra("crop", "true");
		//indicate aspect of desired crop
		cropIntent.putExtra("aspectX", 1);
		cropIntent.putExtra("aspectY", 1);
		//indicate output X and Y
		cropIntent.putExtra("outputX", 256);
		cropIntent.putExtra("outputY", 256);
		//retrieve data on return
		cropIntent.putExtra("return-data", true);
		startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);		
	}

	public Bitmap getBitmap() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inInputShareable = true;
		options.inDither=false;
		options.inTempStorage=new byte[32 * 1024];
		options.inPurgeable = true;
		options.inJustDecodeBounds = false;

		File f = new File(mCurrentPhotoPath);

		FileInputStream fs=null;
		try {
			fs = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			//TODO do something intelligent
			e.printStackTrace();
		}

		Bitmap bm = null;

		try {
			if(fs!=null) bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, options);
		} catch (IOException e) {
			//TODO do something intelligent
			e.printStackTrace();
		} finally{ 
			if(fs!=null) {
				try {
					fs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
		return bm;
	}

	public void rotatePhoto() {
		ExifInterface exif;
		try {
			if(mCurrentPhotoPath == null) {
				mCurrentPhotoPath = contentUri.getPath();
			}
			exif = new ExifInterface(mCurrentPhotoPath);
			int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);	    
			int exifDegree = exifOrientationToDegrees(exifOrientation);
			if(exifDegree != 0) {
				Bitmap bitmap = getBitmap();			    	
				Bitmap rotatePhoto = rotate(bitmap, exifDegree);
				saveBitmap(rotatePhoto);			    			    
			}	    					
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				

	}

	public int exifOrientationToDegrees(int exifOrientation)
	{
		if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
		{
			return 90;
		}
		else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
		{
			return 180;
		}
		else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
		{
			return 270;
		}
		return 0;
	}

	public static Bitmap rotate(Bitmap image, int degrees)
	{
		if(degrees != 0 && image != null)
		{
			Matrix m = new Matrix();
			m.setRotate(degrees, (float)image.getWidth(), (float)image.getHeight());

			try
			{
				Bitmap b = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), m, true);

				if(image != b)
				{
					image.recycle();
					image = b;
				}

				image = b;
			} 
			catch(OutOfMemoryError ex)
			{
				ex.printStackTrace();
			}
		}
		return image;
	}

	public void saveBitmap(Bitmap bitmap) {
		File file = new File(mCurrentPhotoPath);
		
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bitmap.compress(CompressFormat.JPEG, 100, out) ;
		try {
			out.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void onPause(){
		super.onPause();
		SharedPreferences sp= this.getActivity().getSharedPreferences("PrefName", Context.MODE_PRIVATE);
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