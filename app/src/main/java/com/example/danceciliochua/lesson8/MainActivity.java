package com.example.danceciliochua.lesson8;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private ToggleButton mAlarmToggle;
    NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String ACTION_NOTIFY = "com.example.danceciliochua.Lesson8.ACTION_NOTIFY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent notifyIntent = new Intent(ACTION_NOTIFY);

        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Alarm

        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        boolean alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null);



        mAlarmToggle = (ToggleButton) findViewById(R.id.alarmToggle);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mAlarmToggle.setChecked(alarmUp);

        mAlarmToggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String toastMessage;

                        long triggerTime = SystemClock.elapsedRealtime()
                                + 10000;

                        long repeatInterval = 10000;

                        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, repeatInterval, notifyPendingIntent);

                        if(isChecked) {
                            toastMessage = getString(R.string.alarm_on_toast);

                        } else {
                            alarmManager.cancel(notifyPendingIntent);
                            mNotificationManager.cancelAll();

                            toastMessage = getString(R.string.alarm_off_toast);

                        }
                        Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();

                    }

                }

        );



    }

}
