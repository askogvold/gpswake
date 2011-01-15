package no.skogvoldconsulting.gpswake.activities;

import no.skogvoldconsulting.gpswake.R;
import no.skogvoldconsulting.gpswake.model.AlarmDefinition;
import no.skogvoldconsulting.gpswake.model.Position;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EditAlarmActivity extends Activity {

	private static final int EDIT_POSITION = 1;
	private AlarmDefinition alarmDef;
	private TextView alarmNameView;
	private TextView radiusView;
	private Button confirmButton;
	private Button positionButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_alarm);
		alarmDef = (AlarmDefinition) getIntent().getSerializableExtra(
				AlarmDefinition.KEY);

		if (savedInstanceState != null) {
			alarmDef = (AlarmDefinition) savedInstanceState
					.getSerializable(AlarmDefinition.KEY);
		}

		positionButton = (Button) findViewById(R.id.positionButton);
		positionButton.setOnClickListener(new EditPositionListener());
		updatePositionButtonText();

		alarmNameView = (TextView) findViewById(R.id.alarmName);
		alarmNameView.setText(alarmDef.getName());

		radiusView = (TextView) findViewById(R.id.positionRadius);
		radiusView.setText(String.valueOf(alarmDef.getRadius()));

		confirmButton = (Button) findViewById(R.id.confirmButton);
		confirmButton.setOnClickListener(new ConfirmOnClickListener());
	}

	private void updatePositionButtonText() {
		positionButton.setText(alarmDef.getPosition().toString());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(AlarmDefinition.KEY, alarmDef);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}

	public static void createAlarmFromPosition(Activity context,
			int requestCode, Position p) {
		Intent in = new Intent();
		in.setClass(context, EditAlarmActivity.class);
		AlarmDefinition newAlarm = new AlarmDefinition(context
				.getString(R.string.new_alarm), p, 100);
		in.putExtra(AlarmDefinition.KEY, newAlarm);
		context.startActivityForResult(in, requestCode);
	}

	public static void editAlarm(Activity context, int requestCode,
			AlarmDefinition def) {
		Intent in = new Intent(context, EditAlarmActivity.class);
		in.putExtra(AlarmDefinition.KEY, def);
		context.startActivityForResult(in, requestCode);
	}

	private final class EditPositionListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			PickPositionActivity.editPosition(EditAlarmActivity.this,
					EDIT_POSITION, alarmDef.getPosition());
		}
	}

	private final class ConfirmOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			alarmDef.setName(alarmNameView.getText().toString());
			alarmDef.setRadius(Double.valueOf(radiusView.getText().toString()));
			Intent in = new Intent();
			in.putExtra(AlarmDefinition.KEY, alarmDef);
			setResult(RESULT_OK, in);
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case EDIT_POSITION:
			if (resultCode == RESULT_OK) {
				Position p = (Position) data.getSerializableExtra(Position.KEY);
				alarmDef.setPosition(p);
				updatePositionButtonText();
			}
			break;
		}
	}

}