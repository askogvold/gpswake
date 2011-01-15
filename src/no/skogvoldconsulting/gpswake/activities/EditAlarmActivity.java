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
	private AlarmDefinition alarmDef;
	private TextView alarmNameView;
	private TextView radiusView;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			alarmDef = (AlarmDefinition) savedInstanceState
					.getSerializable(AlarmDefinition.KEY);
		}
		
		alarmNameView = (TextView) findViewById(R.id.alarmName);
		alarmNameView.setText(alarmDef.getName());

		radiusView = (TextView) findViewById(R.id.positionRadius);
		radiusView.setText(String.valueOf(alarmDef.getRadius()));

		button = (Button) findViewById(R.id.confirmButton);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alarmDef.setName(alarmNameView.getText().toString());
				alarmDef.setRadius(Double.valueOf(radiusView.getText()
						.toString()));
				Intent in = new Intent();
				in.putExtra(AlarmDefinition.KEY, alarmDef);
				setResult(RESULT_OK, in);
				finish();
			}
		});
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
		AlarmDefinition newAlarm = new AlarmDefinition("Ny alarm", p, 100);
		in.putExtra(AlarmDefinition.KEY, newAlarm);
		context.startActivityForResult(in, requestCode);
	}

	public static void editAlarm(Activity context, int requestCode,
			AlarmDefinition def) {
		Intent in = new Intent(context, EditAlarmActivity.class);
		in.putExtra(AlarmDefinition.KEY, def);
		context.startActivityForResult(in, requestCode);
	}
}