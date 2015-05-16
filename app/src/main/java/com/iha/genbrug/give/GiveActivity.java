package com.iha.genbrug.give;

import android.app.Activity;
import android.content.Intent;
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
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iha.genbrug.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class GiveActivity extends Activity implements View.OnClickListener {

    private enum TouchImageViewState {
        PICK_IMAGE,
        CROP_IMAGE,
        SHOW_IMAGE
    }

    ImageButton btnCamPic;
    ImageButton btnBrowsePic;
    ImageButton btnCancelPic;
    ImageButton btnRotatePic;
    Button btnCropImage;
    View relImageWrapper;
    TextView tvSize;
    ImageView imgView;
    TouchImageView ivChosenImage;
    View layoutHorizontal;
    View layoutVertical;

    Button mBtnUpdate;
    TextView mTvUpdate;

    private static int RESULT_LOAD_IMG = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static long MAX_IMAGE_SIZE = 400000;
    private String mImgDecodableString;
    private String mCurrentPhotoPath;
    private Bitmap mDisplayedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give);

        mBtnUpdate = (Button) findViewById(R.id.btn_update);
        mTvUpdate = (TextView) findViewById(R.id.tv_update);
        mBtnUpdate.setOnClickListener(this);

        layoutHorizontal = findViewById(R.id.layout_crop_grid_horizontal);
        layoutVertical = findViewById(R.id.layout_crop_grid_vertical);

        btnCamPic = (ImageButton) findViewById(R.id.btn_cam_pic);
        btnBrowsePic = (ImageButton) findViewById(R.id.btn_browse_pic);
        btnCancelPic = (ImageButton) findViewById(R.id.btn_cancel_pic);
        btnRotatePic = (ImageButton) findViewById(R.id.btn_rotate_pic);
        btnCropImage = (Button) findViewById(R.id.btn_crop_image);
        relImageWrapper = findViewById(R.id.rel_image_wrapper);
        imgView = (ImageView) findViewById(R.id.image_view);

        // SOURCE: https://github.com/MikeOrtiz/TouchImageView
        ivChosenImage = (TouchImageView) findViewById(R.id.iv_chosen_image);

        tvSize = (TextView) findViewById(R.id.tv_image_size);

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

        setTouchImageViewState(TouchImageViewState.PICK_IMAGE);

        //Typeface font = Typeface.createFromAsset(getAssets(), "Sketchetik-Bold.otf");
        //btnCropImage.setTypeface(font);
    }

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


                tvSize.setText("OptimalInSampleSize: " + optimalInSampleSize + " Orientation: " + rotation + " ImageSizeBefore: " + beforeWidth + " " + beforeHeight + " " + imageByteSize + " ImageSizeAfter: " + rotatedBitmap.getWidth() + " " + rotatedBitmap.getHeight() + " " + rotatedBitmap.getWidth() * rotatedBitmap.getHeight() +
                        " Multiplyer: ");
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

            case R.id.btn_update:
                mTvUpdate.setText(ivChosenImage.getScrollPosition().toString());
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
