package org.biketracker.submit;

import static org.biketracker.submit.NetworkPreferences.isConnectionAvailable;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver {
	
	private static final String TAG = NetworkChangeReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
	    	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    	if(isConnectionAvailable(cm)) {
	        	Log.d(TAG, "Connection available. Waking submitting service");
	        	context.startService(new Intent(context, SubmittingService.class));
	        } 
	    }
	}
}
