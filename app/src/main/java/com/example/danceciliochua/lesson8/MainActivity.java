package com.example.danceciliochua.lesson8;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button notifyButton;
    Button updateButton;
    Button cancelButton;

    private NotificationManager notifyManager;

    private static final int NOTIFICATION_ID = 0;
    private static final String NOTIFICATION_GUIDE_URL = "https://developer.android.com/design/patterns/notifications.html";
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.danceciliochua.lesson8.ACTION_UPDATE_NOTIFICATION";

    NotificationReceiver mReceiver = new NotificationReceiver();

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyButton = (Button) findViewById(R.id.notify);
        updateButton = (Button) findViewById(R.id.update);
        cancelButton = (Button) findViewById(R.id.cancel);

        notifyButton.setEnabled(true);
        updateButton.setEnabled(true);
        cancelButton.setEnabled(true);

        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotification();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotification();
            }
        });

        registerReceiver(mReceiver, new IntentFilter(ACTION_UPDATE_NOTIFICATION));

    }

    public void sendNotification() {
        notifyButton.setEnabled(false);
        updateButton.setEnabled(true);
        cancelButton.setEnabled(true);

        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(NOTIFICATION_GUIDE_URL));
        PendingIntent learnMorePendingIntent = PendingIntent
                .getActivity(this, NOTIFICATION_ID, learnMoreIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.ic_learn_more, "Learn More", learnMorePendingIntent)
                .addAction(R.drawable.ic_update, "Update", updatePendingIntent);

        Notification myNotification = notifyBuilder.build();
        notifyManager.notify(NOTIFICATION_ID, myNotification);

    }

    public void updateNotification() {
        notifyButton.setEnabled(true);
        updateButton.setEnabled(false);
        cancelButton.setEnabled(true);

        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(NOTIFICATION_GUIDE_URL));
        PendingIntent learnMorePendingIntent = PendingIntent
                .getActivity(this, NOTIFICATION_ID, learnMoreIntent, PendingIntent.FLAG_ONE_SHOT);

        Bitmap androidImage = BitmapFactory
                .decodeResource(getResources(),R.drawable.mascot_1);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(androidImage)
                        .setBigContentTitle("Notification Updated!"))
                .addAction(R.drawable.ic_learn_more, "Learn More", learnMorePendingIntent);

        notifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

    }

    public void cancelNotification() {
        notifyButton.setEnabled(true);
        updateButton.setEnabled(true);
        cancelButton.setEnabled(false);

        notifyManager.cancel(NOTIFICATION_ID);

    }

    //BroadcastReceiver
    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification();
        }
    }

}
