package org.biketracker;

import android.location.Location;

public class LocationData {

	private final long time;
	private final double latitude;
	private final double longitude;

	public LocationData(Location location) {
		time = location.getTime();
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

	@Override
	public String toString() {
		return " [time=" + time + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}
}
