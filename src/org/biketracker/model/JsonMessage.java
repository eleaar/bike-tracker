package org.biketracker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.location.Location;

@SuppressWarnings("unused")
public class JsonMessage {
	
	private final UUID uuid;
	private final List<Object[]> locations;

	public JsonMessage(List<Location> locations) {
		List<Object[]> list = new ArrayList<Object[]>();
		for (Location location : locations) {
			list.add(convertLocation(location));
		}
		
		this.locations = list;
		this.uuid = UUID.randomUUID();
	}
	
	private Object[] convertLocation(Location location) {
		return new Object[] { 
				location.getTime(),
				location.getLatitude(), 
				location.getLongitude() };
	}
}
