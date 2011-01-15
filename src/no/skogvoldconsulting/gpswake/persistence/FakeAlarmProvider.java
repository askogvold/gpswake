package no.skogvoldconsulting.gpswake.persistence;

import java.util.ArrayList;

import no.skogvoldconsulting.gpswake.model.AlarmDefinition;
import no.skogvoldconsulting.gpswake.model.Position;

public class FakeAlarmProvider implements AlarmProvider {

	@Override
	public void addAlarm() {
	}

	@Override
	public ArrayList<AlarmDefinition> getAlarms() {
		ArrayList<AlarmDefinition> alarms = new ArrayList<AlarmDefinition>();

		Position origin = new Position(0, 0);
		int radius = 100;
		alarms.add(new AlarmDefinition("Test-alarm 1", origin, radius));
		alarms.add(new AlarmDefinition("Test-alarm 2", origin, radius));
		alarms.add(new AlarmDefinition("Test-alarm 3", origin, radius));
		alarms.add(new AlarmDefinition("Test-alarm 4", origin, radius));
		return alarms;
	}

}
