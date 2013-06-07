package org.biketracker.track;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.biketracker.convert.JsonTrackConverter;
import org.biketracker.convert.TrackConverter;
import org.biketracker.submit.SubmittingService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class TrackingService extends Service {

	private static final String TAG = TrackingService.class.getSimpleName();
	private static final int UPDATE_RATE = 5000;

	private List<Location> locations = new ArrayList<Location>();
	private TrackListener listener = new TrackListener();
	private TrackConverter converter = new JsonTrackConverter();

	@Override
	public void onCreate() {
		super.onCreate();

		Log.d(TAG, "Starting gps listener");

		// Clearing locations just in case
		locations = new ArrayList<Location>();

		// Starting location change listener
		LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		manager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER,
				UPDATE_RATE, 0,
				listener);
		manager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER,
				UPDATE_RATE, 0,
				listener);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "Stopping gps listener.");

		// Stopping location change listener
		LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		manager.removeUpdates(listener);

		if(!locations.isEmpty()) {
			final List<Location> currentLocations = locations;
			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			final Intent intent = new Intent(this, SubmittingService.class);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// Saving locations into shared preferences
					UUID uuid = UUID.randomUUID();
					preferences
						.edit()
						.putString(uuid.toString(), converter.convert(uuid, currentLocations))
						.commit();
	
					// Starting the submitting service
					startService(intent);
				}
			}).start();
		}

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private class TrackListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			locations.add(location);
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
	}
}
