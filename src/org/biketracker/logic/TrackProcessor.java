package org.biketracker.logic;

import java.util.List;

import org.biketracker.model.JsonMessage;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.location.Location;

public class TrackProcessor {

	private final String serverUrl = "http://requestb.in/19yx6ds1";
	private RestTemplate restTemplate;

	public TrackProcessor() {
		restTemplate = new RestTemplate(true);
		restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
	}

	public void submit(List<Location> locations) {
		restTemplate.put(serverUrl, new JsonMessage(locations));
	}
}
