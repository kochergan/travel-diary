package mobop.traveldiary.pd;

public final class Location {
	private final int id;
    private final String title;
    private final long tmstmp;
    private final String text;
    //private final String image;
    private final double latitude;
    private final double longitude;

    public Location(int id, String title, long tmstmp, String text, double latitude, double longitude) {
    	this.id = id;
        this.title = title;
        this.tmstmp = tmstmp;
        this.text = text;
        //this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(int id, String title, long tmstmp) {
    	this(id, title, tmstmp, null, 0.0, 0.0);
    }
    
    public Location(int id) {
    	this(id, null, 0, null, 0.0, 0.0);
    }
    
    public Location(String title, long tmstmp, String text, double latitude, double longitude) {
    	this(-1, title, tmstmp, text, latitude, longitude);
    }

    public int getId() {
    	return id;
    }

    public String getTitle() {
    	return title;
    }

    public long getTmstmp() {
    	return tmstmp;
    }

    public String getText() {
    	return text;
    }

    public double getLatitude() {
    	return latitude;
    }

    public double getLongitude() {
    	return longitude;
    }
}