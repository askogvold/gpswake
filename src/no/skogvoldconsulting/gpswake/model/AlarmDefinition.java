package no.skogvoldconsulting.gpswake.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AlarmDefinition implements Serializable {
	private String name;
	public Position position;
	public float radius;

	public AlarmDefinition(String name, Position position,
			float maxDistanceInMeters) {
		super();
		this.name = name;
		this.position = position;
		this.radius = maxDistanceInMeters;
	}

	public String getName() {
		return name;
	}


}
