package org.biketracker.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

import android.location.Location;

public class JsonTrackConverter implements TrackConverter {

	@Override
	public String convert(UUID uuid, List<Location> locations) {
		return new Gson().toJson(new Wrapper(uuid, locations));
	}

	@SuppressWarnings("unused")
	private static class Wrapper {

		private final UUID uuid;
		private final List<Object[]> locations;

		public Wrapper(UUID uuid, List<Location> locations) {
			List<Object[]> list = new ArrayList<Object[]>();
			for (Location location : locations) {
				list.add(convertLocation(location));
			}

			this.locations = list;
			this.uuid = uuid;
		}

		private Object[] convertLocation(Location location) {
			return new Object[] { 
					location.getTime(), 
					location.getLatitude(),
					location.getLongitude() };
		}
	}
}
