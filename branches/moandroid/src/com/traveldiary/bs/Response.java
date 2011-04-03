package com.traveldiary.bs;

import com.traveldiary.pd.Location;

public class Response {
	public static final int STATUS_ERROR = 0;
	public static final int STATUS_OK = 1;

	private int status = -1;
	private int type = -1;
	private Location location = null;
	private String explanation = null;
	private String additionalinfo = null;

	public Response(int status, int type, Location location, String expl,
			String add) {
		this.status = status;
		this.location = location;
		this.setExplanation(expl);
		this.setAdditionalinfo(add);
	}

	public Response(int status, int type, Location location) {
		this(status, type, location, "", "");
	}

	public Response(int status, int type, Location location, String add) {
		this(status, type, location, "", add);
	}

	public Response(int status, String expl, String add) {
		this(status, -1, null, expl, add);
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setAdditionalinfo(String additionalinfo) {
		this.additionalinfo = additionalinfo;
	}

	public String getAdditionalinfo() {
		return additionalinfo;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
