package org.biketracker.logic;

import java.util.List;

import org.biketracker.model.JsonMessage;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.location.Location;

public class TrackProcessor {

	private final String serverUrl;
	private RestTemplate restTemplate;

	public TrackProcessor(String serverUrl) {
		this.serverUrl = serverUrl;
		restTemplate = new RestTemplate(true);
		restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
	}

	public void submit(List<Location> locations) {
		restTemplate.put(serverUrl, new JsonMessage(locations));
	}
}
