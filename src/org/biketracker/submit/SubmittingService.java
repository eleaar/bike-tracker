package org.biketracker.submit;

import static java.lang.String.format;
import static org.biketracker.submit.NetworkPreferences.isConnectionAvailable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.biketracker.R;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class SubmittingService extends IntentService {

	private static final String TAG = SubmittingService.class.getSimpleName();
	private static final String NO_TRACK = "empty";

	private String serverUrl;
	private HttpHeaders headers;
	private RestTemplate restTemplate;

	public SubmittingService() {
		super(TAG);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		serverUrl = getString(R.string.server_url);
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "Waking up");

		// Check if we are connected
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (isConnectionAvailable(cm)){

			// Retrieve all saved track names
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			Set<String> tracks = new HashSet<String>(preferences.getAll().keySet());
			
			if(!tracks.isEmpty()) {
				Log.d(TAG, format("Trying to submit %d tracks", tracks.size()));
				int successCounter = 0;
	
				// Process tracks while network is still available
				for (Iterator<String> iter = tracks.iterator(); iter.hasNext() && isConnectionAvailable(cm);) {
	
					// read next track
					String trackName = iter.next();
					String trackData = preferences.getString(trackName, NO_TRACK);
	
					// check in case it just got deleted
					if (!NO_TRACK.equals(trackData)) {
						try {
							// post to server and remove if successful
							restTemplate.postForObject(serverUrl, new HttpEntity<String>(trackData, headers), String.class);
							preferences.edit().remove(trackName).commit();
							successCounter++;
						} catch (RestClientException e) {
							Log.e(TAG, "Could not post track. Will retry later", e);
						}
					}
				}
				Log.d(TAG, format("Submitted successfully %d tracks", successCounter));
			} else {
				Log.d(TAG, "No tracks to be submitted");
			}
		} else {
			Log.d(TAG, "Network was unavailable. Will retry later");
		}
	}
}
