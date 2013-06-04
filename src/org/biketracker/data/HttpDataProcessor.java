package org.biketracker.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.biketracker.model.LocationData;
import org.json.JSONObject;

import android.os.Looper;

public class HttpDataProcessor implements DataProcessor {

	private List<LocationData> data = new ArrayList<LocationData>();
	private UUID id;
	private String serverUrl;

	public HttpDataProcessor(UUID id, String serverUrl) {
		this.id = id;
		this.serverUrl = serverUrl;
	}

	@Override
	public void append(LocationData locationData) {
		data.add(locationData);
	}

	@Override
	public void flush() {
		new Thread() {
			public void run() {
				Looper.prepare(); // For Preparing Message Pool for the child
									// Thread
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						10000); // Timeout Limit
				JSONObject json = new JSONObject();

				try {
					HttpPost post = new HttpPost(serverUrl);
					json.put("id", id);
					json.put("locations", data);
					StringEntity se = new StringEntity(json.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json"));
					post.setEntity(se);
					client.execute(post);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Looper.loop(); // Loop in the message queue
			}
		}.start();
	}
}
