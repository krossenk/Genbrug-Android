package com.iha.genbrug.give;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.iha.genbrug.ServerService;

import com.iha.genbrug.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import webservice.Publication;


public class GiveActivity extends Activity implements View.OnClickListener {

    private enum TouchImageViewState {
        PICK_IMAGE,
        CROP_IMAGE,
        SHOW_IMAGE
    }

    private Publication pub = new Publication();

    static final int DIALOG_ID = 0;
    int hour, minute;

    InterceptScrollView interceptScrollview;
    ImageButton btnCamPic;
    ImageButton btnBrowsePic;
    ImageButton btnCancelPic;
    ImageButton btnRotatePic;
    Button btnCropImage;
    Button btnGive;
    View relImageWrapper;
    TouchImageView ivChosenImage;
    View layoutHorizontal;
    View layoutVertical;

    // FORM Views
    Spinner spinnerCategories;
    EditText etHeadline;
    EditText etDescription;
    Spinner spinnerPickupType;
    Button btnPickStartTime;
    Button btnPickEndTime;

    private static int RESULT_LOAD_IMG = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static long MAX_IMAGE_SIZE = 400000;
    private String mImgDecodableString;
    private String mCurrentPhotoPath;
    private Bitmap mDisplayedBitmap;
    private ServerService serverService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServerService.LocalBinder binder = (ServerService.LocalBinder) service;
            serverService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give);

        Intent intent = new Intent(this,ServerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        interceptScrollview = (InterceptScrollView) findViewById(R.id.intercept_scrollview);

        layoutHorizontal = findViewById(R.id.layout_crop_grid_horizontal);
        layoutVertical = findViewById(R.id.layout_crop_grid_vertical);

        btnCamPic = (ImageButton) findViewById(R.id.btn_cam_pic);
        btnBrowsePic = (ImageButton) findViewById(R.id.btn_browse_pic);
        btnCancelPic = (ImageButton) findViewById(R.id.btn_cancel_pic);
        btnRotatePic = (ImageButton) findViewById(R.id.btn_rotate_pic);
        btnCropImage = (Button) findViewById(R.id.btn_crop_image);
        btnGive = (Button) findViewById(R.id.btn_give);
        relImageWrapper = findViewById(R.id.rel_image_wrapper);

        // SPINNER CATEGORIES
        List<String> Categories =  Arrays.asList("Jonas", "Bikes", "Furniture", "Clothes", "Books", "Select category");
        spinnerCategories = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter<String> catAdapter = new CategoriesSpinnerAdapter(this, R.layout.categories_dropdown_item_layout, Categories);
        spinnerCategories.setAdapter(catAdapter);
        spinnerCategories.setSelection(Categories.size()-1);

        // SPINNER PICKUP TYPE
        List<String> PickupTypes = Arrays.asList("Home", "Street", "Select pickup type");
        spinnerPickupType = (Spinner) findViewById(R.id.spinner_pickup_type);
        ArrayAdapter<String> pickupAdapter = new PickupTypeSpinnerAdapter(this, R.layout.pickup_type_dropdown_item_layout, PickupTypes);
        spinnerPickupType.setAdapter(pickupAdapter);
        spinnerPickupType.setSelection(PickupTypes.size()-1);


        // HEADLINE FIELD
        etHeadline = (EditText) findViewById(R.id.et_headline);
        // DESCRIPTION FIELD
        etDescription = (EditText) findViewById(R.id.et_description);
        // BUTTON PICK START TIME
        btnPickStartTime = (Button) findViewById(R.id.btn_pick_start_time);
        // BUTTON PICK END TIME
        btnPickEndTime = (Button) findViewById(R.id.btn_pick_end_time);

        // Create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> categoriesAdapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        //categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
       // spinnerCategories.setAdapter(categoriesAdapter);

        // SOURCE: https://github.com/MikeOrtiz/TouchImageView
        ivChosenImage = (TouchImageView) findViewById(R.id.iv_chosen_image);

        // Square the background image dynamically
        // SOURCE: http://stackoverflow.com/questions/9798392/imageview-have-height-match-width
        ivChosenImage.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams mParams;
                mParams = (RelativeLayout.LayoutParams) relImageWrapper.getLayoutParams();
                mParams.height = ivChosenImage.getWidth();
                relImageWrapper.setLayoutParams(mParams);
                relImageWrapper.postInvalidate();
            }
        });

        btnCamPic.setOnClickListener(this);
        btnBrowsePic.setOnClickListener(this);
        btnCancelPic.setOnClickListener(this);
        btnRotatePic.setOnClickListener(this);
        btnCropImage.setOnClickListener(this);
        btnGive.setOnClickListener(this);
        btnPickStartTime.setOnClickListener(this);
        btnPickEndTime.setOnClickListener(this);

        setTouchImageViewState(TouchImageViewState.PICK_IMAGE);

        //Typeface font = Typeface.createFromAsset(getAssets(), "Sketchetik-Bold.otf");
        //btnCropImage.setTypeface(font);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id){
            case DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListener, hour, minute, true );
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i2) {
            hour = i;
            minute = i2;
            Toast.makeText(getBaseContext(), "Set time is: " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
        }
    };

    private int getImageRotation(String path){
        ExifInterface exif;
        int exifOrientation = 0;
        try {
            exif = new ExifInterface( path );
            exifOrientation = exif.getAttributeInt( ExifInterface.TAG_ORIENTATION, 1 );
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        int rotate = 0;
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                rotate = 45;
                break;
            default:
                break;
        }
        return rotate;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // IMPORTANT: INCLUDE THIS IN SOURCES, used example from following tutorial
        // http://programmerguru.com/android-tutorial/how-to-pick-image-from-gallery/
        try{
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row (return value indicates if query result is empty)
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mImgDecodableString = cursor.getString(columnIndex);
                cursor.close();
            }
            // Result from picture taken by camera
            else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

                File imgFile = new  File(mCurrentPhotoPath);

                if(imgFile.exists()){
                    mImgDecodableString = imgFile.getAbsolutePath();
                }

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }

            if((requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) ||
                    (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)){
                BitmapFactory.Options options = new BitmapFactory.Options();
                File f = new File(mImgDecodableString);
                long imageByteSize = f.length();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mImgDecodableString, options);
                int beforeWidth = options.outWidth;
                int beforeHeight = options.outHeight;
                double divider = MAX_IMAGE_SIZE < beforeWidth*beforeHeight ? ((double)beforeWidth*beforeHeight / MAX_IMAGE_SIZE) : 1;

                int optimalInSampleSize = (int) Math.round((Math.sqrt(divider)));
                int i = 1;
                while(i < optimalInSampleSize) {
                    i *= 2;
                }

                options.inSampleSize = i;
                options.inJustDecodeBounds = false;

                // Decode image to a Bitmap
                Bitmap bitM = BitmapFactory.decodeFile(mImgDecodableString, options);

                // Get rotation and prepare matrix
                int rotation = getImageRotation(mImgDecodableString);
                Matrix rotationMatrix = new Matrix();
                rotationMatrix.postRotate(rotation);

                // Create rotated bitmap
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitM, 0, 0, bitM.getWidth(), bitM.getHeight(), rotationMatrix, true);

                setmDisplayedBitmap(rotatedBitmap);

                setTouchImageViewState(TouchImageViewState.CROP_IMAGE);
            }

        }catch(Exception e){

        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cam_pic:
                takePictureIntent();
                break;
            case R.id.btn_browse_pic:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(isExternalStorageWritable()) {
                    startActivityForResult(intent, RESULT_LOAD_IMG);
                }
                break;
            case R.id.btn_cancel_pic:
                setTouchImageViewState(TouchImageViewState.PICK_IMAGE);
                break;
            case R.id.btn_crop_image:
                RectF zoomedRect = ivChosenImage.getZoomedRect();

                int x0 = (int)(zoomedRect.left * mDisplayedBitmap.getWidth());
                int x1 = (int)(zoomedRect.right * mDisplayedBitmap.getWidth());
                int y0 = (int)(zoomedRect.top * mDisplayedBitmap.getHeight());
                int y1 = (int)(zoomedRect.bottom * mDisplayedBitmap.getHeight());
                int width = x1 - x0;
                int height = y1 - y0;

                Bitmap croppedImage = Bitmap.createBitmap(mDisplayedBitmap, x0, y0, width, height);
                setmDisplayedBitmap(croppedImage);

                setTouchImageViewState(TouchImageViewState.SHOW_IMAGE);
                break;
            case R.id.btn_give:
                if(populatePublication()){
                    try {
                        serverService.createPublication(pub);
                    }catch(Exception e){ }
                }
                break;
            case R.id.btn_rotate_pic:
                PointF lastPoint = ivChosenImage.getScrollPosition();
                float tempX = lastPoint.x;
                float tempY = lastPoint.y;
                Matrix rotationMatrix = new Matrix();
                rotationMatrix.postRotate(90);
                Bitmap rotated = Bitmap.createBitmap(mDisplayedBitmap, 0, 0, mDisplayedBitmap.getWidth(), mDisplayedBitmap.getHeight(), rotationMatrix, true);
                setmDisplayedBitmap(rotated);
                ivChosenImage.setScrollPosition((1-tempY), tempX);
                break;
            case R.id.btn_pick_start_time:
                final Dialog pstDialog = new Dialog(this);
                pstDialog.setTitle("Select earliest pickup time");
                pstDialog.setContentView(R.layout.custom_dialog_date_time);
                pstDialog.show();

                // ---------------------------------------------------------------------------------
                // DAY DATE PICKER (using a NumberPicker)
                // ---------------------------------------------------------------------------------
                final NumberPicker npDayDate = (NumberPicker) pstDialog.findViewById(R.id.np_day_date);

                final String[] dayDateValues = new String[14];
                final String[] dayDatePubValues = new String[14];
                final Date date = new Date(); // Get today's date

                for(int i = 0; i < dayDateValues.length; i++){
                    Calendar c = Calendar.getInstance();
                    Date futureDate = new Date();
                    c.setTime(date);            // Set today's date to calender
                    c.add(Calendar.DATE, i);    // Increment date by i
                    futureDate = c.getTime();

                    SimpleDateFormat sdf = new SimpleDateFormat("EE, MMM dd"); // Format as "Tue, Jul 21"
                    dayDateValues[i] = sdf.format(futureDate);  // Add day date to values
                    SimpleDateFormat sdfPub = new SimpleDateFormat("dd-MM-yyyy");
                    dayDatePubValues[i] = sdfPub.format(futureDate);
                }
                npDayDate.setMaxValue(dayDateValues.length - 1);
                npDayDate.setMinValue(0);
                npDayDate.setDisplayedValues(dayDateValues);
                npDayDate.setWrapSelectorWheel(false);

                // ---------------------------------------------------------------------------------
                // TIME PICKER
                // ---------------------------------------------------------------------------------
                final TimePicker tpTime = (TimePicker) pstDialog.findViewById(R.id.tp_time);
                tpTime.setIs24HourView(true); // Enable 24 hour mode

                // ---------------------------------------------------------------------------------
                // IF USER ALREADY PICKED VALUE, START AT SAME PLACE
                // ---------------------------------------------------------------------------------
                if(pub.pickupStartime != null) // If user has already picked a time
                {
                    // Get the NumberPicker index for the date that was chosen previously
                    int previousIndex = Arrays.asList(dayDatePubValues).indexOf(pub.pickupStartime.substring(0, 10));
                    npDayDate.setValue(previousIndex); // Set this index to be the current value showing

                    // Remove any zeroes in front of number, then remove any non-number characters before parsing to integer
                    tpTime.setCurrentHour(Integer.parseInt(pub.pickupStartime.substring(11,13).replaceFirst("^0+(?!$)", "").replaceAll("[\\D]","")));
                    tpTime.setCurrentMinute(Integer.parseInt(pub.pickupStartime.substring(14,16).replaceFirst("^0+(?!$)", "").replaceAll("[\\D]","")));
                }

                // ---------------------------------------------------------------------------------
                // DIALOG BUTTONS
                // ---------------------------------------------------------------------------------
                Button btnSubmitDialog = (Button) pstDialog.findViewById(R.id.btn_submit_dialog);
                Button btnCancelDialog = (Button) pstDialog.findViewById(R.id.btn_cancel_dialog);

                btnSubmitDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String zeroPadMin = String.format("%02d", tpTime.getCurrentMinute());
                        String zeroPadHour = String.format("%02d", tpTime.getCurrentHour());
                        btnPickStartTime.setText(dayDateValues[npDayDate.getValue()]  + " " + zeroPadHour + ":" + zeroPadMin);
                        pub.pickupStartime = dayDatePubValues[npDayDate.getValue()] + ":" + zeroPadHour + ":" + zeroPadMin + ":" + "00";
                        pstDialog.cancel();
                    }
                });

                btnCancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pstDialog.cancel();
                    }
                });

                break;
            case R.id.btn_pick_end_time:
                final Dialog petDialog = new Dialog(this);
                petDialog.setTitle("Select latest pickup time");
                petDialog.setContentView(R.layout.custom_dialog_date_time);
                petDialog.show();

                // ---------------------------------------------------------------------------------
                // DAY DATE PICKER (using a NumberPicker)
                // ---------------------------------------------------------------------------------
                final NumberPicker npDayDate2 = (NumberPicker) petDialog.findViewById(R.id.np_day_date);

                final String[] dayDateValues2 = new String[14];
                final String[] dayDatePubValues2 = new String[14];
                final Date date2 = new Date(); // Get today's date

                for(int i = 0; i < dayDateValues2.length; i++){
                    Calendar c = Calendar.getInstance();
                    Date futureDate = new Date();
                    c.setTime(date2);            // Set today's date to calender
                    c.add(Calendar.DATE, i);    // Increment date by i
                    futureDate = c.getTime();

                    SimpleDateFormat sdf = new SimpleDateFormat("EE, MMM dd"); // Format as "Tue, Jul 21"
                    dayDateValues2[i] = sdf.format(futureDate);  // Add day date to values
                    SimpleDateFormat sdfPub = new SimpleDateFormat("dd-MM-yyyy");
                    dayDatePubValues2[i] = sdfPub.format(futureDate);
                }
                npDayDate2.setMaxValue(dayDateValues2.length - 1);
                npDayDate2.setMinValue(0);
                npDayDate2.setDisplayedValues(dayDateValues2);
                npDayDate2.setWrapSelectorWheel(false);

                // ---------------------------------------------------------------------------------
                // TIME PICKER
                // ---------------------------------------------------------------------------------
                final TimePicker tpTime2 = (TimePicker) petDialog.findViewById(R.id.tp_time);
                tpTime2.setIs24HourView(true); // Enable 24 hour mode

                // ---------------------------------------------------------------------------------
                // IF USER ALREADY PICKED VALUE, START AT SAME PLACE
                // ---------------------------------------------------------------------------------
                if(pub.pickupEndtime != null) // If user has already picked a time
                {
                    // Get the NumberPicker index for the date that was chosen previously
                    int previousIndex = Arrays.asList(dayDatePubValues2).indexOf(pub.pickupEndtime.substring(0, 10));
                    npDayDate2.setValue(previousIndex); // Set this index to be the current value showing

                    // Remove any zeroes in front of number, then remove any non-number characters before parsing to integer
                    tpTime2.setCurrentHour(Integer.parseInt(pub.pickupEndtime.substring(11,13).replaceFirst("^0+(?!$)", "").replaceAll("[\\D]","")));
                    tpTime2.setCurrentMinute(Integer.parseInt(pub.pickupEndtime.substring(14,16).replaceFirst("^0+(?!$)", "").replaceAll("[\\D]","")));
                }

                // ---------------------------------------------------------------------------------
                // DIALOG BUTTONS
                // ---------------------------------------------------------------------------------
                Button btnSubmitDialog2 = (Button) petDialog.findViewById(R.id.btn_submit_dialog);
                Button btnCancelDialog2 = (Button) petDialog.findViewById(R.id.btn_cancel_dialog);

                btnSubmitDialog2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String zeroPadMin = String.format("%02d", tpTime2.getCurrentMinute());
                        String zeroPadHour = String.format("%02d", tpTime2.getCurrentHour());
                        btnPickEndTime.setText(dayDateValues2[npDayDate2.getValue()]  + " " + zeroPadHour + ":" + zeroPadMin);
                        pub.pickupEndtime = dayDatePubValues2[npDayDate2.getValue()] + ":" + zeroPadHour + ":" + zeroPadMin + ":" + "00";
                        petDialog.cancel();
                    }
                });

                btnCancelDialog2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        petDialog.cancel();
                    }
                });

                break;
        }
    }

    /**
     * Set the displayed bitmap to empty and set background image to TouchImageView
     */
    private void setmDisplayedBitmap(){
        mDisplayedBitmap = null;
        ivChosenImage.setImageResource(R.drawable.old_cam);
    }

    /**
     * Set the displayed bitmap and set the same bitmap to TouchImageView
     */
    private void setmDisplayedBitmap(Bitmap bitM){
        mDisplayedBitmap = bitM;
        ivChosenImage.setImageBitmap(mDisplayedBitmap);
    }

    /**
     * Creates intent that starts camera if an activity can handle the intent.
     * resolveActivity() returns null if activity can't handle intent.
     */
    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private boolean populatePublication(){

        pub.title = etHeadline.getText().toString();
        pub.description = etDescription.getText().toString();

        if( pub.title.length() > 0 && pub.description.length() > 0 &&
            pub.pickupStartime != null && pub.pickupEndtime != null )
        {
            return true;
        }
        return false;
    }

    private boolean publishImage(){



        return false;
    }

    /**
     *
     *
     * @return ImagePhotoFile
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setTouchImageViewState(final TouchImageViewState state) {
        switch (state){
            case PICK_IMAGE:
                layoutHorizontal.setVisibility(View.GONE);
                layoutVertical.setVisibility(View.GONE);
                btnCropImage.setVisibility(View.GONE);
                btnBrowsePic.setVisibility(View.VISIBLE);
                btnCancelPic.setVisibility(View.GONE);
                btnRotatePic.setVisibility(View.GONE);
                btnCamPic.setVisibility(View.VISIBLE);
                setmDisplayedBitmap();
                ivChosenImage.resetZoom();
                ivChosenImage.setEnabled(false);
                break;
            case CROP_IMAGE:
                layoutHorizontal.setVisibility(View.VISIBLE);
                layoutVertical.setVisibility(View.VISIBLE);
                btnCropImage.setVisibility(View.VISIBLE);
                btnBrowsePic.setVisibility(View.GONE);
                btnCancelPic.setVisibility(View.VISIBLE);
                btnRotatePic.setVisibility(View.VISIBLE);
                btnCamPic.setVisibility(View.GONE);
                ivChosenImage.setEnabled(true);
                break;
            case SHOW_IMAGE:
                layoutHorizontal.setVisibility(View.GONE);
                layoutVertical.setVisibility(View.GONE);
                btnCropImage.setVisibility(View.GONE);
                ivChosenImage.setEnabled(false);
                ivChosenImage.resetZoom();
                break;
        }
    }


}
