package org.biketracker.logic;

import java.util.List;

import org.biketracker.model.JsonMessage;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class TrackProcessor {

	private final String serverUrl;
	private RestTemplate restTemplate;

	public TrackProcessor(String serverUrl) {
		this.serverUrl = serverUrl;
		restTemplate = new RestTemplate(true);
		restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
	}

	public void submit(List<Location> locations) {
		try {
			restTemplate.put(serverUrl, new JsonMessage(locations));
		} catch(RestClientException e) {
			Log.e(TrackProcessor.class.getSimpleName(), e.toString());
		}
	}
}
