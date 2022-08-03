package com.wht.addekho.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wht.addekho.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Random;

public class FirebaseMessageService extends FirebaseMessagingService {


    private String image_path, json_image_path;
    private DatabaseSqliteHandler db;
    private JSONObject jsonObject;
    public static final String INTENT_FILTER = "INTENT_FILTER";
    private String title = "Ravi Masale", message1, image;
    MediaPlayer player;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //   Log.d("Mytag1", "call:onMessageReceived 1" + remoteMessage.getNotification());
        //  Log.d("Mytag2", "call:onMessageReceived 1" + remoteMessage.getNotification());
        Log.d("Mytag1", "call:onMessageReceived 1" + remoteMessage.getData());


        try {

        /*    player = MediaPlayer.create(this, R.raw.bell_alert_tone);
            player.start();*/
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(1000);
            }

            jsonObject = new JSONObject(remoteMessage.getData());
            Log.d("Mytag1", "call:onMessageReceived 1" + remoteMessage.getData());


            message1 = jsonObject.getString("body");
            json_image_path = jsonObject.getString("image");
            title = jsonObject.getString("title");

            db = DatabaseSqliteHandler.getInstance(this);
            if (json_image_path != null && !json_image_path.isEmpty() && !json_image_path.equals("null") && !json_image_path.equals("")) {
                db.insert_notification(title, message1, json_image_path);
            } else {

                db.insert_notification(title, message1, null);
            }

            // {body=hellow, image=, title=Hello}


            //Log.d("countN", "onMessageReceived: "+db.getNotificationCount());
            showNotification(message1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Intent intent = new Intent(INTENT_FILTER);
       // sendBroadcast(intent);

       // registerReceiver(myReceiver, new IntentFilter(FirebaseMessagingService.INTENT_FILTER));
    }

    private void showNotification(String message) {

        Intent intent = new Intent(this, ActivityNotification.class);
        Bundle bundle = new Bundle();
        // bundle.putInt(Constants.FRAGMENT_ID, R.id.nav_notification);
        intent.putExtras(bundle);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK /*| Intent.FLAG_ACTIVITY_CLEAR_TASK*/);
        // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        int requestCode = ("someString" + System.currentTimeMillis()).hashCode();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
       /*Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getPackageName() + "/" + R.raw.sound);*/

       /* Bitmap bigPicture = BitmapFactory.decodeResource(getResources(), R.drawable.user_background);
        String contentText = "A classic image processing test image.";
        String summaryText = "This mandrill is often used as a test image.";
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                .setSummaryText(summaryText)
                .bigPicture(bigPicture);*/

        //Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.default_square);

       /* if (jsonObject.has("image")) {
            Bitmap image = getBitmapFromURL(this.image);
            builder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(image).setSummaryText(message));
        }*/
  /*      NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentInfo(message)
                .setContentText(message)
                .setLargeIcon(icon)
                .setSmallIcon(R.drawable.default_square)
                .setChannelId("vijendra")
                .setSound(soundUri)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentIntent(pendingIntent);
*/
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

       /* Notification builder1 = new NotificationCompat.Builder(this, "vijendra")
                .setSmallIcon(R.drawable.logo)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentTitle(title)
                .setContentText(message)
                *//*.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))*//*
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image).setSummaryText(message))
                .setSound(uri)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setContentIntent(pendingIntent)
                .build();*/

        Notification builder=null;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (jsonObject.has("image")) {
            Bitmap image = getBitmapFromURL(this.image);
             builder = new NotificationCompat.Builder(this, "vijendra")
                    .setSmallIcon(R.drawable.no_data_found)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setContentTitle(title)
                    .setContentText(message)
                    /*.setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message))*/
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(image).setSummaryText(message))
                    .setSound(uri)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLights(Color.RED, 3000, 3000)
                    .setContentIntent(pendingIntent)
                    .build();
        }else
        {
             builder = new NotificationCompat.Builder(this, "vijendra")
                    .setSmallIcon(R.drawable.no_data_found)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message))
                    .setSound(uri)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLights(Color.RED, 3000, 3000)
                    .setContentIntent(pendingIntent)
                    .build();
        }
        /* manager.notify(requestCode, builder.build());*/

        //Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.notification_mp3);

       /* Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[] {1000, 100, 1000, 100, 1000}, 1);
// start sound manually
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notif1);
        Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
        ringtone.play();*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("vijendra", "Your Notifications",
                    NotificationManager.IMPORTANCE_HIGH);
            // NotificationChannel notificationChannel = manager.getNotificationChannel("vijendra");

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();


            // notificationChannel.setDescription("");
            // notificationChannel.setSound(uri, attributes);
            notificationChannel.setLockscreenVisibility(NotificationCompat.PRIORITY_HIGH);
            notificationChannel.setVibrationPattern(new long[]{2000});
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            manager.createNotificationChannel(notificationChannel);
            Log.d("abcd", "IFshowNotification: ");
            notificationChannel.canBypassDnd();
        }

      /*  int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        manager.notify(m, builder);*/

        // MediaPlayer mp= MediaPlayer.create(FirebaseMessageService.this, R.raw.notificationsound);
        // mp.start();


        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel("vijendra");
            manager.createNotificationChannel(channel);
            channel.canBypassDnd();
            Log.d("abcd", "IFshowNotificationDND: ");
        }
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        manager.notify(m, builder);

        /*if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(""pushNotification);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), YourActivity.class);
            resultIntent.putExtra("message", message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
            }
        }*/
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    public void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Libas/Notification Images/");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        image_path = file.toString();
        Log.d("path-->", image_path);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("FIREBASE_TOKEN", "onNewToken: " + s);
    }
}