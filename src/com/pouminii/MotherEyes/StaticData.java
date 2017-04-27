package com.pouminii.MotherEyes;

import android.graphics.Typeface;

public class StaticData{
	private static StaticData instance;
	
	public int disWidth;
	public int disHeight;
	public static StaticData GetInstance(){
		return instance;
	}
	static {
		instance = new StaticData();
	}
}