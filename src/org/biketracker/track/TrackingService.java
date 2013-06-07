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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class TrackingService extends Service {

	private static final String TAG = TrackingService.class.getSimpleName();

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
				5000, 0,
				listener);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "Stopping gps listener.");

		// Stopping location change listener
		LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		manager.removeUpdates(listener);

		// Saving locations into shared preferences
		UUID uuid = UUID.randomUUID();
		PreferenceManager
				.getDefaultSharedPreferences(this)
				.edit()
				.putString(uuid.toString(), converter.convert(uuid, locations))
				.commit();

		// Starting the submitting service
		startService(new Intent(this, SubmittingService.class));

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
