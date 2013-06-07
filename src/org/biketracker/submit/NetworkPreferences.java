package org.biketracker.submit;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkPreferences {

	public static boolean isConnectionAvailable(ConnectivityManager cm) {
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// Currently we only send data over wifi, to minimize user costs
		return networkInfo != null
				&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI
				&& networkInfo.isConnected();
	}
}
