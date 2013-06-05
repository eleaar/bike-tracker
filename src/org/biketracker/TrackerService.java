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

	private final TrackProcessor processor = new TrackProcessor();
	private final TrackListener listener = new TrackListener(processor);
	private final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
		manager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 
				1000, 0,
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
