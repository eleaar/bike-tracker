package org.biketracker;

import java.util.UUID;

import org.biketracker.data.DataProcessor;
import org.biketracker.data.HttpDataProcessor;
import org.biketracker.model.LocationData;
import org.biketracker.util.LocationAdapter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class TrackerService extends Service {

	private static final String TAG = TrackerService.class.getSimpleName();
	private static final String SERVER_URL = "http://todo.com";

	private LocationListener locationListener;
	private DataProcessor dataProcessor;

	@Override
	public void onCreate() {
		super.onCreate();
		startGpsListener();
	}

	@Override
	public void onDestroy() {
		stopGpsListener();
		super.onDestroy();
	}

	private void startGpsListener() {
		Log.d(TAG, "Starting gps listener");
		dataProcessor = new HttpDataProcessor(UUID.randomUUID(), SERVER_URL);
		locationListener = new LocationAdapter() {
			@Override
			public void onLocationChanged(Location location) {
				dataProcessor.append(new LocationData(location));
			}
		};
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,
				locationListener);
	}

	private void stopGpsListener() {
		Log.d(TAG, "Stopping gps listener.");
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.removeUpdates(locationListener);
		dataProcessor.flush();
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
