package no.skogvoldconsulting.gpswake.model;

@SuppressWarnings("serial")
public class NewAlarmDefinition extends AlarmDefinition {
	private static NewAlarmDefinition singleton = new NewAlarmDefinition();

	private NewAlarmDefinition() {
		super("Legg til ny...", null, 0);
	}

	public static NewAlarmDefinition get() {
		return singleton;
	}
}
