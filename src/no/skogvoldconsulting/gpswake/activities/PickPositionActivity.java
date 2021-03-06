package no.skogvoldconsulting.gpswake.activities;

import no.skogvoldconsulting.gpswake.R;
import no.skogvoldconsulting.gpswake.model.Position;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import de.android1.overlaymanager.ManagedOverlay;
import de.android1.overlaymanager.ManagedOverlayItem;
import de.android1.overlaymanager.OverlayManager;
import de.android1.overlaymanager.ZoomEvent;
import de.android1.overlaymanager.ManagedOverlayGestureDetector.OnOverlayGestureListener;

public class PickPositionActivity extends MapActivity {
	private MapView mapView;
	private MyLocationOverlay mlo;
	private ManagedOverlayItem selectedPosition;
	private ManagedOverlay ol;
	private OverlayManager om;
	private Button confirmButton;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.pick_position);
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);

		setupConfirmButton();

		setupMyLocation();
		setupClickHandling();

		Position inputPosition = (Position) getIntent().getSerializableExtra(
				Position.KEY);
		if (inputPosition != null) {
			setSelectedPosition(inputPosition.toGeoPoint());
		}

		if (icicle != null && icicle.containsKey(Position.KEY)) {
			Position pos = (Position) icicle.getSerializable(Position.KEY);
			setSelectedPosition(pos.toGeoPoint());
		}
	}

	private void setupConfirmButton() {
		confirmButton = (Button) findViewById(R.id.confirmButton);
		confirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent();
				GeoPoint point = selectedPosition.getPoint();
				in.putExtra(Position.KEY, Position.fromGeoPoint(point));
				setResult(RESULT_OK, in);
				finish();
			}
		});
	}

	private void setSelectedPosition(GeoPoint pos) {
		if (ol != null) {
			ol.remove(selectedPosition);
			selectedPosition = ol.createItem(pos, "Clicked position");
			mapView.postInvalidate();
		}
		confirmButton.setEnabled(true);
		mapView.getController().animateTo(pos);
	}

	@Override
	protected void onResume() {
		super.onResume();
		followMyLocation();
	}

	private void followMyLocation() {
		mlo.enableMyLocation();
		if (selectedPosition == null) {
			mlo.runOnFirstFix(new Runnable() {
				public void run() {
					mapView.getController().animateTo(mlo.getMyLocation());
				}
			});
		}
	}

	@Override
	protected void onPause() {
		mlo.disableMyLocation();
		super.onPause();
	}

	private void setupClickHandling() {
		om = new OverlayManager(this, mapView);
		ol = om.createOverlay();
		om.populate();

		ol.setOnOverlayGestureListener(new GestureListener());
	}

	private void setupMyLocation() {
		mlo = new MyLocationOverlay(this, mapView);
		mlo.enableMyLocation();
		mapView.getOverlays().add(mlo);
	}

	protected void clickedPosition(GeoPoint pos) {
		Toast.makeText(this, pos.toString(), Toast.LENGTH_LONG).show();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (ol != null) {
			GeoPoint pos = selectedPosition.getPoint();
			outState.putSerializable(Position.KEY, Position.fromGeoPoint(pos));
		}
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}

	public static void requestPosition(Activity context, int requestCode) {
		Intent in = new Intent(context, PickPositionActivity.class);
		context.startActivityForResult(in, requestCode);
	}

	public static void editPosition(Activity context, int requestCode,
			Position p) {
		Intent in = new Intent(context, PickPositionActivity.class);
		in.putExtra(Position.KEY, p);
		context.startActivityForResult(in, requestCode);
	}

	private final class GestureListener implements OnOverlayGestureListener {
		@Override
		public boolean onZoom(ZoomEvent arg0, ManagedOverlay arg1) {
			return false;
		}

		@Override
		public boolean onSingleTap(MotionEvent ev, ManagedOverlay ol,
				GeoPoint pos, ManagedOverlayItem item) {
			setSelectedPosition(pos);
			return true;
		}

		@Override
		public boolean onScrolled(MotionEvent arg0, MotionEvent arg1,
				float arg2, float arg3, ManagedOverlay arg4) {
			return false;
		}

		@Override
		public void onLongPressFinished(MotionEvent arg0, ManagedOverlay arg1,
				GeoPoint arg2, ManagedOverlayItem arg3) {
		}

		@Override
		public void onLongPress(MotionEvent arg0, ManagedOverlay arg1) {
		}

		@Override
		public boolean onDoubleTap(MotionEvent arg0, ManagedOverlay arg1,
				GeoPoint arg2, ManagedOverlayItem arg3) {
			return false;
		}
	}
}