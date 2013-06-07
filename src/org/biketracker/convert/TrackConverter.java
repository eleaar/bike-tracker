package org.biketracker.convert;

import java.util.List;
import java.util.UUID;

import android.location.Location;

public interface TrackConverter {
	String convert(UUID uuid, List<Location> locations);
}
