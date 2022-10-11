package ch.firealarmtv.objects;

public class Hydrant {
	
	private String id; //Id number of hydrant
	private double lat; // Latitude coordinate of hydrant
	private double lon; // Longitude coordinate of hydrant

	/**
	 * @param id
	 * @param lat
	 * @param lon
	 */
	public Hydrant(String id, double lat, double lon) {
		super();
		this.id = id;
		this.lat = lat;
		this.lon = lon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
	
	
	
	

}
