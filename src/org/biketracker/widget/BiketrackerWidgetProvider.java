package org.biketracker.widget;

import org.biketracker.R;
import org.biketracker.service.BiketrackerService;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class BiketrackerWidgetProvider extends AppWidgetProvider {

	private RemoteViews views;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		Log.i("provider", "on update:" + appWidgetIds.length);
		for (int i = 0, N = appWidgetIds.length; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	@Override
	public void onEnabled(Context context) {
		views = new RemoteViews(context.getPackageName(),
				R.layout.biketracker_appwidget);

		Intent intent = new Intent(BiketrackerService.ACTION_START_TRACKING,
				null, context, BiketrackerService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, 0);
		views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

		// TODO Auto-generated method stub
		super.onEnabled(context);
		Log.i("provider", "on enabled");
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		Log.i("provider", "on disabled");
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
		Log.i("provider", "on deleted");
	}

}
