package org.biketracker.logic;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;

public class TrackListener implements LocationListener {

	private final TrackProcessor processor;
	
	private List<Location> locations = new ArrayList<Location>();

	public TrackListener(TrackProcessor processor) {
		this.processor = processor;
	}

	@Override
	public void onLocationChanged(Location location) {
		locations.add(location);
	}

	@SuppressWarnings("unchecked")
	public void flush() {
		new AsyncTask<List<Location>, Void, Void>() {
			
			@Override
			protected Void doInBackground(List<Location>... locations) {
				processor.submit(locations[0]);
				return null;
			}
		}.execute(locations);
		locations = new ArrayList<Location>();
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
