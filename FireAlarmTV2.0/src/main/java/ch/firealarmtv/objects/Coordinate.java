package ch.firealarmtv.objects;

import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;

import ch.firealarmtv.handlers.JsonHandler;

public class Coordinate {

	private double lat;
	private double lon;
	private int type;
	public static final int WGS84 = 1;
	public static final int LV95 = 2;

	/**
	 * @param lat
	 * @param lon
	 * @param type
	 */
	public Coordinate(double lat, double lon, int type) {
		super();
		this.lat = lat;
		this.lon = lon;
		this.type = type;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Coordinate getWGS84() {

		if (this.type == Coordinate.WGS84) {
			return this;
		}
		String urlAPI = "https://geodesy.geo.admin.ch/reframe/lv95towgs84";

		String urlStr = urlAPI + "?easting=" + this.lon + "&northing=" + this.lat + "&format=json";

		JsonNode response = null;
		try {
			response = JsonHandler.getJson(new URL(urlStr));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double newLat = response.get("northing").asDouble();
		double newLon = response.get("easting").asDouble();

		return new Coordinate(newLat, newLon, Coordinate.WGS84);

	}

	public Coordinate getLV95() {

		if (this.type == Coordinate.LV95) {
			return this;
		}
		String urlAPI = "https://geodesy.geo.admin.ch/reframe/wgs84tolv95";

		String urlStr = urlAPI + "?easting=" + this.lon + "&northing=" + this.lat + "&format=json";

		JsonNode response = null;
		try {
			response = JsonHandler.getJson(new URL(urlStr));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double newLat = response.get("northing").asDouble();
		double newLon = response.get("easting").asDouble();

		return new Coordinate(newLat, newLon, Coordinate.WGS84);

	}

}
