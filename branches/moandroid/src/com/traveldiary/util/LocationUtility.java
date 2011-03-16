package com.traveldiary.util;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public abstract class LocationUtility {
	public static Location GetLastKnownLocation(Context context) {
		Location location = null;
		Location temp = null;
		boolean enabled = false;
		LocationManager manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = manager.getAllProviders();

		for (String provider : providers) {
			if (enabled && !manager.isProviderEnabled(provider))
				continue;

			temp = manager.getLastKnownLocation(provider);

			if (temp != null) {
				if (location == null
						|| location.getAccuracy() < temp.getAccuracy()) {
					location = temp;
					enabled = manager.isProviderEnabled(provider);
				}
			}
		}

		return location;
	}
}