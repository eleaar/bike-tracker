package org.biketracker;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

public class TrackerService extends Service {

	private static final String TAG = TrackerService.class.getSimpleName();

	private static final String SERVER_URL = "http://todo.com";

	private UUID id;
	private List<LocationData> locations;
	private LocationListener locationListener;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "on Create.");
		id = UUID.randomUUID();
		locations = new ArrayList<LocationData>();
		locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				handleLocationUpdate(location);
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
		};
		startGpsListener();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "on Destroy.");
		stopGpsListener();
		flushLocations();
		super.onDestroy();
	}

	private void startGpsListener() {
		Log.i(TAG, "starting gps sensor listener.");
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,
				locationListener);
	}

	private void stopGpsListener() {
		Log.i(TAG, "stopping gps sensor listener.");
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.removeUpdates(locationListener);
	}
	
	private void handleLocationUpdate(Location location) {
		Log.d(TAG, "new location: " + location);
		locations.add(new LocationData(location));
	}

	private void flushLocations() {
        Thread t = new Thread() {
            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;
                JSONObject json = new JSONObject();

                try {
                    HttpPost post = new HttpPost(SERVER_URL);
                    json.put("id", id);
                    json.put("locations", locations);
                    StringEntity se = new StringEntity( json.toString());  
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                    /*Checking response */
                    if(response!=null){
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                }
                Looper.loop(); //Loop in the message queue
            }
        };
        t.start();      
    }
	
	public class TrackerServiceBinder extends Binder {
		TrackerServiceBinder getService() {
			return TrackerServiceBinder.this;
		}
	}

	private final Binder binder = new TrackerServiceBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
}
