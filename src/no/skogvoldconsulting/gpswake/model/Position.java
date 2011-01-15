package no.skogvoldconsulting.gpswake.model;

import java.io.Serializable;

import com.google.android.maps.GeoPoint;

@SuppressWarnings("serial")
public class Position implements Serializable {
	public double lat;
	public double lon;

	public Position(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
	public static Position fromGeoPoint(GeoPoint gp) {
		return new Position(gp.getLatitudeE6()/1e6, gp.getLongitudeE6()/1e6);
	}
	
	public GeoPoint toGeoPoint() {
		
		int latitudeE6 = (int) (lat*1e6);
		int longitudeE6 = (int) (lon*1e6);
		return new GeoPoint(latitudeE6, longitudeE6);
	}

	@Override
	public String toString() {
		String latLetter = (lat >= 0) ? "N" : "S";
		String lonLetter = (lon >= 0) ? "E" : "W";
		return String.format("%s %f %s %f", latLetter, Math.abs(lat),
				lonLetter, Math.abs(lon));
	}
}