package no.skogvoldconsulting.gpswake;

import no.skogvoldconsulting.gpswake.model.AlarmDefinition;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.IBinder;

public class WakeupService extends Service {

	private static final String POSITION_REACHED = "POSITION_REACHED";
	public static final String ALARM_DEF = "ALARM_DEF";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		AlarmDefinition alarmDef = (AlarmDefinition) intent.getExtras()
				.getSerializable(ALARM_DEF);

		LocationManager locationService = (LocationManager) getSystemService(LOCATION_SERVICE);
		long noExpiration = -1;
		double latitude = alarmDef.getPosition().lat;
		double longitude = alarmDef.getPosition().lon;
		double radius = alarmDef.getRadius();

		PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(
				POSITION_REACHED), 0);
		registerReceiver(new AlarmReceiver(),
				new IntentFilter(POSITION_REACHED));

		locationService.addProximityAlert(latitude, longitude, (float)radius,
				noExpiration, pi);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class AlarmReceiver extends BroadcastReceiver {

		private static final int NOTIFICATION_ID = 0;

		@Override
		public void onReceive(Context context, Intent intent) {
			
			String alarmText = "Alarm!";
			int icon = android.R.drawable.ic_dialog_alert;
			long when = System.currentTimeMillis();
			Notification not = new Notification(icon, alarmText, when);
			not.defaults = Notification.DEFAULT_ALL;
			
			NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			nm.notify(NOTIFICATION_ID, not);
		}
	}
}