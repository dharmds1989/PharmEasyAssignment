package com.dk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	public static boolean isNetworkAvailable(Context mContext) {
		boolean isNetworkAvailable = false;
		if(mContext != null) {
			ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			isNetworkAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();
		}
		return isNetworkAvailable;
	}
}
