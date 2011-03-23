package com.traveldiary.pd;

import java.io.InputStream;
import java.util.Date;

public final class Location {
	private final long id;
	private final String title;
	private final Date tmstmp;
	private final String text;
	private final InputStream image;
	private final double latitude;
	private final double longitude;

	public Location(long id, String title, Date tmstmp, String text,
			InputStream image, double latitude, double longitude) {
		this.id = id;
		this.title = title;
		this.tmstmp = tmstmp;
		this.text = text;
		this.image = image;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Location(long id, String title, Date tmstmp) {
		this(id, title, tmstmp, null, null, 0.0, 0.0);
	}

	public Location(long id) {
		this(id, "", new Date(), null, null, 0.0, 0.0);
	}

	public long getId() {
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

	public InputStream getImage() {
		return image;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
}
