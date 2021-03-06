package no.skogvoldconsulting.gpswake.activities;

import java.util.ArrayList;

import no.skogvoldconsulting.gpswake.R;
import no.skogvoldconsulting.gpswake.model.AlarmDefinition;
import no.skogvoldconsulting.gpswake.model.NewAlarmDefinition;
import no.skogvoldconsulting.gpswake.model.Position;
import no.skogvoldconsulting.gpswake.persistence.AlarmProvider;
import no.skogvoldconsulting.gpswake.persistence.FakeAlarmProvider;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends Activity {
	private static final int POSITION_NEW = 1;
	private static final int ALARM_NEW = 2;
	private static final int ALARM_EDIT = 3;

	private AlarmProvider provider = new FakeAlarmProvider();
	private ArrayAdapter<AlarmDefinition> adapter;
	private Spinner spinner;
	private Button b;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		b = (Button) findViewById(R.id.editAlarmButton);
		b.setOnClickListener(new EditAlarmButtonListener());

		populateSpinner();
	}

	private void populateSpinner() {
		spinner = (Spinner) findViewById(R.id.choosePlaceSpinner);
		populateAlarms();
		spinner.setOnItemSelectedListener(new AlarmOnItemListener());
	}

	private void requestNewPosition() {
		PickPositionActivity.requestPosition(this, POSITION_NEW);
	}

	private void createAlarmFromPosition(Position p) {
		EditAlarmActivity.createAlarmFromPosition(this, ALARM_NEW, p);
	}

	private void editAlarm(AlarmDefinition def) {
		EditAlarmActivity.editAlarm(this, ALARM_EDIT, def);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case POSITION_NEW:
			if (resultCode == RESULT_OK) {
				Position p = (Position) data.getSerializableExtra(Position.KEY);
				createAlarmFromPosition(p);
			} else {
				spinner.setSelection(0);
			}
			break;
		case ALARM_NEW:
			if (resultCode == RESULT_OK) {
				AlarmDefinition def = (AlarmDefinition) data
						.getSerializableExtra(AlarmDefinition.KEY);
				provider.addAlarm(def);
				populateAlarms();
				spinner.setSelection(adapter.getPosition(def));
			}
			break;
		case ALARM_EDIT:
			if(resultCode == RESULT_OK) {
				
			}
			break;
		}
	}

	private void populateAlarms() {
		adapter = new ArrayAdapter<AlarmDefinition>(this,
				android.R.layout.simple_spinner_item);
		ArrayList<AlarmDefinition> alarms = provider.getAlarms();
		for (AlarmDefinition a : alarms) {
			adapter.add(a);
		}
		adapter.add(NewAlarmDefinition.get());
		spinner.setAdapter(adapter);
	}

	private final class AlarmOnItemListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			AlarmDefinition item = (AlarmDefinition) parent
					.getItemAtPosition(pos);
			if (item instanceof NewAlarmDefinition) {
				parent.setSelection(0);
				requestNewPosition();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	private final class EditAlarmButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			editAlarm((AlarmDefinition) spinner.getSelectedItem());
		}
	}
}