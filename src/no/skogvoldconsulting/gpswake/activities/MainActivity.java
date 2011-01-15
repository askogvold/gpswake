package no.skogvoldconsulting.gpswake.activities;

import no.skogvoldconsulting.gpswake.R;
import no.skogvoldconsulting.gpswake.model.Position;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final int NEW_POSITION = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button b = (Button) findViewById(R.id.pickPosButton);

		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestNewPosition();
			}

		});
	}

	private void requestNewPosition() {
		Intent in = new Intent();
		in.setClass(MainActivity.this, PickPositionActivity.class);
		startActivityForResult(in, NEW_POSITION);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case NEW_POSITION:
			if (resultCode == RESULT_OK) {
				Position p = (Position) data
						.getSerializableExtra(PickPositionActivity.POSITION);
				Toast.makeText(this, p.toString(), Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
}