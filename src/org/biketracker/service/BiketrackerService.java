package org.biketracker.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BiketrackerService extends Service {

	public static final String ACTION_START_TRACKING = "org.biketracker.action.START_TRACKING";
	public static final String ACTION_STOP_TRACKING = "org.biketracker.action.STOP_TRACKING";

	private boolean isTracking = false;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handleIntent(intent);
		return START_STICKY;
	}

	private void handleIntent(Intent intent) {
		String action = intent.getAction();
		if (ACTION_START_TRACKING.equals(action) && !isTracking()) {
			startTracking();
		} else if (ACTION_STOP_TRACKING.equals(action) && isTracking()) {
			stopTracking();
		}
	}

	private void startTracking() {
		Log.i("tracker", "start tracking");
		isTracking = true;
	}

	private void stopTracking() {
		Log.i("tracker", "stop tracking");
		isTracking = false;
	}

	private boolean isTracking() {
		return isTracking;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
