package no.skogvoldconsulting.gpswake.persistence;

import java.util.ArrayList;

import no.skogvoldconsulting.gpswake.model.AlarmDefinition;

public interface AlarmProvider {
	public ArrayList<AlarmDefinition> getAlarms();
	public void addAlarm();
}
