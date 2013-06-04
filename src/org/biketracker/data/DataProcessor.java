package org.biketracker.data;

import org.biketracker.model.LocationData;

public interface DataProcessor {
	void append(LocationData locationData);
	void flush();
}
