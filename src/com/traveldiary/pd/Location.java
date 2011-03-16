package com.traveldiary.pd;

import java.util.Date;

import android.graphics.Bitmap;

public final class Location {
	private final int id;
	private final String title;
	private final Date tmstmp;
	private final String text;
	private final Bitmap image;
	private final double latitude;
	private final double longitude;

	public Location(int id, String title, Date tmstmp, String text, Bitmap image, double latitude, double longitude) {
		this.id = id;
		this.title = title;
		this.tmstmp = tmstmp;
		this.text = text;
		this.image = image;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Location(int id, String title, Date tmstmp) {
		this(id, title, tmstmp, null, null, 0.0, 0.0);
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Date getTmstmp() {
		return tmstmp;
	}

	public String getText() {
		return text;
	}

	public Bitmap getImage() {
		return image;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
}
