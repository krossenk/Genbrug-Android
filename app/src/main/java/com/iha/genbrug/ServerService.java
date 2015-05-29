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
import webservice.getPublicationSubscriptionsResponse;
import webservice.getAllCategoriesResponse;
import webservice.getUserSubscriptionsResponse;

/**
 * Created by Parsa on 16/05/2015.
 */
public class ServerService extends Service {

    public static final String RESULT_RETURNED_FROM_SERVICE = "Result_Returned_From_Service";
    public static final String START_CREATE_PUBLICATION_RESULT = "Result_Returned_From_startCreatePublication";
    public static final String ALL_PUBLICATIONS_RESULT = "Result_Returned_From_getAllPublications";
    public static final String ALL_PUBLICATIONSUBSRIPTIONS_RESULT = "Result_Returned_From_getPublicationSubscriptions";
    public static final String ALL_USERSUBSRIPTIONS_RESULT = "Result_Returned_From_getUserSubscriptions";
    public static final String ALL_CATEGORIES_RESULT = "Result_Returned_From_getAllCategories";
    public static final String IMAGE_RETURN_URL = "Result_Returned_From_Saveimage";
    public static final String PUBLICATIONRESULT = "Result_Returned_From_getPublication";

    private Thread servicecallthread = null;
    private final IBinder mBinder = new LocalBinder();
    private RecycleWebService recycleService = new RecycleWebService();

    private User validatedUser;
    private User returnUser;
    private Publication returnPublication;
    private getPublicationSubscriptionsResponse publicationSubscriptionsList;
    private getUserSubscriptionsResponse userSubscriptionsResponseList;
    private getAllCategoriesResponse getAllCategoriesResponseList;
    private getAllPublicationsResponse publicationsResponseList;
    private String imageResponseURL;
    private long publicationId;
    private long userId;

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
    //method for validate user on DB
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

    // method for creating user on DB
    public void createUser(final User usr) throws Exception {
        servicecallthread = new Thread() {
            public void run() {
                try {
                    userId = recycleService.createUser(usr);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        this.servicecallthread.start();

    }

    public long getUserId()
    {
        return userId;
    }


    public User getValidatedUser ()
    {
        return validatedUser;
    }

    public void updateUser(final User user)
    {
        servicecallthread = new Thread() {
            public void run() {
                try {
                    recycleService.updateUser(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.servicecallthread.start();
    }

    public void createSubscription(final long userId, final long publicationId)
    {
        servicecallthread = new Thread() {
            public void run() {
                try {
                    recycleService.createSubscription(userId,publicationId );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.servicecallthread.start();
    }
    public void startGetUser(final long userId)
    {
        servicecallthread = new Thread() {
            public void run() {
                try {
                    returnUser = recycleService.getUser(userId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.servicecallthread.start();
    }

    public User getReturnedUser()
    {
        return returnUser;
    }

    public void startCreatePublication(final Publication pub)
    {
        servicecallthread = new Thread() {
            public void run() {
                try {
                    publicationId = recycleService.createPublication(pub);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent retint = new Intent(START_CREATE_PUBLICATION_RESULT);
                sendBroadcast(retint);
            }
        };
        this.servicecallthread.start();
    }

    public long getReturnedPublicationId()
    {
        return publicationId;
    }

    public void startGetPublication(final long publicationId)
    {
        servicecallthread = new Thread() {
            public void run() {
                try {
                    returnPublication = recycleService.getPublication(publicationId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent retint = new Intent(PUBLICATIONRESULT);
                sendBroadcast(retint);

            }
        };
        this.servicecallthread.start();
    }

    public Publication getReturnedPublication()
    {
        return returnPublication;
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

    public void startGetPublicationSubscriptions(final long publicationId)
    {
        servicecallthread = new Thread() {
            public void run() {
                try {
                    publicationSubscriptionsList = recycleService.getPublicationSubscriptions(publicationId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent retint = new Intent(ALL_PUBLICATIONSUBSRIPTIONS_RESULT);
                sendBroadcast(retint);
//
            }
        };
        this.servicecallthread.start();
    }

    public getPublicationSubscriptionsResponse getPublicationSubscriptions()
    {
        return publicationSubscriptionsList;
    }

    public void startGetUserSubscriptions(final long userId)
    {
        servicecallthread = new Thread() {
            public void run() {
                try {
                    userSubscriptionsResponseList = recycleService.getUserSubscriptions(userId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent retint = new Intent(ALL_USERSUBSRIPTIONS_RESULT);
                sendBroadcast(retint);
//
            }
        };
        this.servicecallthread.start();
    }

    public getUserSubscriptionsResponse getUserSubscriptions()
    {
        return userSubscriptionsResponseList;
    }

    public void startGetAllCategories()
    {
        servicecallthread = new Thread() {
            public void run() {
                try {
                    getAllCategoriesResponseList = recycleService.getAllCategories();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent retint = new Intent(ALL_CATEGORIES_RESULT);
                sendBroadcast(retint);
//
            }
        };
        this.servicecallthread.start();
    }

    public getAllCategoriesResponse getAllCategories()
    {
        return getAllCategoriesResponseList;
    }


    public void startSavingImage(final String filename, final String imgData, final long publicationId)
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
