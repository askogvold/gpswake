package no.skogvoldconsulting.gpswake.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AlarmDefinition implements Serializable {
	public static String KEY = "ALARM_DEF";
	private String name;
	private Position position;
	private double radius;

	public AlarmDefinition(String name, Position position,
			float maxDistanceInMeters) {
		super();
		this.setName(name);
		this.setPosition(position);
		this.setRadius(maxDistanceInMeters);
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

	public double getRadius() {
		return radius;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public void setName(String name) {
		this.name = name;
	}

}
