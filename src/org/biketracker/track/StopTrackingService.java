package org.biketracker.track;

import org.biketracker.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class StopTrackingService extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stopService(new Intent(this, TrackingService.class));
		Toast.makeText(this, R.string.toast_stopping, Toast.LENGTH_SHORT).show();
		finish();
	}
}
