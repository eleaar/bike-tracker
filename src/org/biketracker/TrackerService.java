package org.biketracker;

import org.biketracker.logic.TrackListener;
import org.biketracker.logic.TrackProcessor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

public class TrackerService extends Service {

	private static final String TAG = TrackerService.class.getSimpleName();

	private TrackProcessor processor;
	private TrackListener listener;
	private LocationManager manager;

	@Override
	public void onCreate() {
		super.onCreate();
		initializeComponents();
		startGpsListener();
	}

	@Override
	public void onDestroy() {
		stopGpsListener();
		super.onDestroy();
	}

	private void initializeComponents() {
		processor = new TrackProcessor(getString(R.string.server_url));
		listener = new TrackListener(processor);
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	private void startGpsListener() {
		Log.d(TAG, "Starting gps listener");
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,
				listener);
	}

	private void stopGpsListener() {
		Log.d(TAG, "Stopping gps listener.");
		manager.removeUpdates(listener);
		listener.flush();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
