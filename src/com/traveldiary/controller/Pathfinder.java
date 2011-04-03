package com.traveldiary.controller;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

public class Pathfinder extends MapActivity {
	private MapView mapView;
	private MapController mapController;
	private Overlay overlay;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.location_map);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(false);
		mapController = mapView.getController();
		mapController.setZoom(14);
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				5, new GeoUpdateHandler());
		initLocation();
		Drawable drawable = this.getResources().getDrawable(R.drawable.point);
		overlay = new Overlay(drawable);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public class GeoUpdateHandler implements LocationListener {
		public void onLocationChanged(Location location) {
			int lat = (int) (location.getLatitude() * 1E6);
			int lng = (int) (location.getLongitude() * 1E6);
			mapController.animateTo(new GeoPoint(lat, lng));
			createMarker();
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	private void initLocation() {
		MyLocationOverlay overlay = new MyLocationOverlay(this, mapView);
		overlay.enableMyLocation();
		overlay.enableCompass();
		mapView.getOverlays().add(overlay);
	}

	private void createMarker() {
		GeoPoint point = mapView.getMapCenter();
		OverlayItem item = new OverlayItem(point, "", "");
		overlay.addOverlay(item);
		mapView.getOverlays().add(overlay);
	}
}