package com.iha.genbrug;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import webservice.RecycleWebService;
import webservice.User;

/**
 * Created by Parsa on 16/05/2015.
 */
public class ServerService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private RecycleWebService recycleService = new RecycleWebService();
    private User validatetUser;
    public static final String RESULT_RETURNED_FROM_SERVICE = "Result_Returned_From_Service";
    private Thread servicecallthread = null;


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void sendNotification(String myString) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(myString)
                        .setContentText("Something interesting happened");
        int NOTIFICATION_ID = 12345;

        /*Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);*/
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void validateUser(final String usrname, final String pass) throws Exception {

        servicecallthread = new Thread() {
            public void run() {
                try {
                    validatetUser = recycleService.validateUser(usrname, pass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent retint = new Intent(RESULT_RETURNED_FROM_SERVICE);
                sendBroadcast(retint);
//
            }
        };
        this.servicecallthread.start();

    }

    public void createUser(User usr) throws Exception {
        recycleService.createUser(usr);
    }

    public User getValidatedUser ()
    {
        return validatetUser;
    }



    public class LocalBinder extends Binder {
        ServerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServerService.this;
        }
    }
}
