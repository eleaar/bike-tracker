package org.biketracker.widget;

import org.biketracker.Launcher;
import org.biketracker.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class BiketrackerWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		Intent intent = new Intent(context, Launcher.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.biketracker_appwidget);
		views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

		for (int i = 0, N = appWidgetIds.length; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
}
