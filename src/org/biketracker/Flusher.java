package org.biketracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Flusher extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stopService(new Intent(this, TrackerService.class));
		Toast.makeText(this, "Stopping bike tracking", Toast.LENGTH_SHORT).show();
		finish();
	}
}
