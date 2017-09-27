package com.xhtek.hxaccount.util;

import java.util.UUID;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneDevice {

	public static String getDxDeviceId(Context context){
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    final String tmDevice, tmSerial, tmPhone, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    String uniqueId = deviceUuid.toString();
		return uniqueId;
	}

	public static String getSimpleDeviceId(Context context){
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    final String tmDevice;
	    tmDevice = "" + tm.getDeviceId();
		return tmDevice;
	}

}
