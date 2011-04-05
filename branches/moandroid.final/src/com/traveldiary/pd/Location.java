package com.traveldiary.pd;

import java.util.Date;

public final class Location {
	private final long id;
	private final String title;
	private final String text;
	private final long tmstmp;
	private final double latitude;
	private final double longitude;

	public Location(long id, String title, String text, long tmstmp,
			double latitude, double longitude) {
		this.id = id;
		this.title = title;
		this.text = text;
		this.tmstmp = tmstmp;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Location(String title, String text, long tmstmp, double latitude,
			double longitude) {
		this(-1, title, text, tmstmp, latitude, longitude);
	}

	public Location(long id, String title, long tmstmp) {
		this(id, title, "", tmstmp, 0.0, 0.0);
	}

	public Location(long id) {
		this(id, "", "", new Date().getTime(), 0.0, 0.0);
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public long getTmstmp() {
		return tmstmp;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
}
