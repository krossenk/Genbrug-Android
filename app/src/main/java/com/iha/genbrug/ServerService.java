package com.iha.genbrug;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import webservice.Publication;
import webservice.RecycleWebService;
import webservice.User;
import webservice.getAllPublicationsResponse;

/**
 * Created by Parsa on 16/05/2015.
 */
public class ServerService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private RecycleWebService recycleService = new RecycleWebService();
    private User validatedUser;
    public static final String RESULT_RETURNED_FROM_SERVICE = "Result_Returned_From_Service";
    public static final String ALL_PUBLICATIONS_RESULT = "Result_Returned_From_getAllPublications";
    public static final String IMAGE_RETURN_URL = "Result_Returned_From_Saveimage";
    private Thread servicecallthread = null;
    private getAllPublicationsResponse publicationsResponseList;
    private String imageResponseURL;

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
                    validatedUser = recycleService.validateUser(usrname, pass);
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

    public void createUser(final User usr) throws Exception {
        servicecallthread = new Thread() {
            public void run() {
                try {
                    recycleService.createUser(usr);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        this.servicecallthread.start();

    }

    public User getValidatedUser ()
    {
        return validatedUser;
    }

    public void createPublication(final Publication pub)
    {
        servicecallthread = new Thread() {
            public void run() {
                try {
                    recycleService.createPublication(pub);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.servicecallthread.start();
    }

    public void startGetAllPublications()
    {
        servicecallthread = new Thread() {
            public void run() {
                try {
                    publicationsResponseList = recycleService.getAllPublications();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent retint = new Intent(ALL_PUBLICATIONS_RESULT);
                sendBroadcast(retint);
//
            }
        };
        this.servicecallthread.start();
    }

    public getAllPublicationsResponse getAllPublications()
    {
        return publicationsResponseList;
    }

    public void startSavingImage(final String filename, final byte[] imgData, final int publicationId)
    {
        servicecallthread = new Thread() {
            public void run() {
                try {
                   imageResponseURL = recycleService.saveImage(filename,imgData,publicationId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent retint = new Intent(IMAGE_RETURN_URL);
                sendBroadcast(retint);
//
            }
        };
        this.servicecallthread.start();
    }

    public String getLatestImageURL()
    {
        return imageResponseURL;
    }



    public class LocalBinder extends Binder {
        public ServerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServerService.this;
        }
    }
}
