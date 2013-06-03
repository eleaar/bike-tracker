package org.biketracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class TrackerService extends Service {

	private static final String TAG = TrackerService.class.getSimpleName();

	private LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			Log.d(TAG, "new location: " + location);
		}

		@Override
		public void onProviderDisabled(String arg0) {
		}

		@Override
		public void onProviderEnabled(String arg0) {
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "on Create.");
		startGpsListener();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "on Destroy.");
		stopGpsListener();
		super.onDestroy();
	}

	private void startGpsListener() {
		Log.i(TAG, "starting gps sensor listener.");
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,
				locationListener);
	}

	private void stopGpsListener() {
		Log.i(TAG, "stopping gps sensor listener.");
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.removeUpdates(locationListener);
	}

	public class TrackerServiceBinder extends Binder {
		TrackerServiceBinder getService() {
			return TrackerServiceBinder.this;
		}
	}

	private final Binder binder = new TrackerServiceBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
}
