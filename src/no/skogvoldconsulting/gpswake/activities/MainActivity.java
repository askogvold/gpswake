package no.skogvoldconsulting.gpswake.activities;

import java.util.ArrayList;

import no.skogvoldconsulting.gpswake.R;
import no.skogvoldconsulting.gpswake.model.AlarmDefinition;
import no.skogvoldconsulting.gpswake.model.NewAlarmDefinition;
import no.skogvoldconsulting.gpswake.model.Position;
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
	private static final int POSITION_EDIT = 2;
	private static final int ALARM_NEW = 3;
	private static final int ALARM_EDIT = 4;
	
	private ArrayAdapter<AlarmDefinition> adap;
	private Spinner s;
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
		s = (Spinner) findViewById(R.id.choosePlaceSpinner);
		adap = new ArrayAdapter<AlarmDefinition>(this,
				android.R.layout.simple_spinner_item);
		ArrayList<AlarmDefinition> alarms = new FakeAlarmProvider().getAlarms();
		for(AlarmDefinition a : alarms) {
			adap.add(a);
		}
		adap.add(NewAlarmDefinition.get());
		s.setOnItemSelectedListener(new AlarmOnItemListener());

		s.setAdapter(adap);
	}

	private void requestNewPosition() {
		PickPositionActivity.requestPosition(this, POSITION_NEW);
	}
	
	private void editPosition(Position p) {
		PickPositionActivity.editPosition(this, POSITION_EDIT, p);
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
				Position p = (Position) data
						.getSerializableExtra(Position.KEY);
				createAlarmFromPosition(p);
			} else {
				s.setSelection(0);
			}
			break;
		}
	}


	private final class AlarmOnItemListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			AlarmDefinition item = (AlarmDefinition) parent
					.getItemAtPosition(pos);
			if (item instanceof NewAlarmDefinition) {
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
			editAlarm((AlarmDefinition) s.getSelectedItem());
		}
	}
}