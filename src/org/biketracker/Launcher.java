/*
 * Software is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * 
 * The Initial Developer of the Original Code is Paweł Kamiński.
 * All Rights Reserved.
 */
/**
 *
 * date        : 2011-07-25
 * author      : pawel
 * file name   : Launcher.java
 *
 * description :
 *
 */
package org.biketracker;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Launcher extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startTrackingService();
		showNotification();
		finish();
	}

	private void startTrackingService() {
		startService(new Intent(this, TrackerService.class));
	}

	private void showNotification() {
		Intent resultIntent = new Intent(this, Flusher.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification note = new Notification(
				R.drawable.bike, 
				"Bike Tracker",
				System.currentTimeMillis());
		note.setLatestEventInfo(
				this, 
				"BikeTracker", 
				"Click to stop tracking",
				pendingIntent);
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, note);
	}
}
