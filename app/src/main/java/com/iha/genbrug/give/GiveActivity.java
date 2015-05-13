package com.iha.genbrug.give;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iha.genbrug.R;

import java.io.File;
import java.io.IOException;


public class GiveActivity extends Activity implements View.OnClickListener {

    ImageButton btnCamPic;
    ImageButton btnBrowsePic;
    View relImageWrapper;
    TextView tvSize;
    ImageView imgView;
    TouchImageView ivChosenImage;

    private static int RESULT_LOAD_IMG = 1;
    private static long MAX_IMAGE_SIZE = 150000;
    private String imgDecodableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give);

        btnCamPic = (ImageButton) findViewById(R.id.btn_cam_pic);
        btnBrowsePic = (ImageButton) findViewById(R.id.btn_browse_pic);
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
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                BitmapFactory.Options options = new BitmapFactory.Options();
                File f = new File(imgDecodableString);
                long imageByteSize = f.length();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imgDecodableString, options);
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
                Bitmap bitM = BitmapFactory.decodeFile(imgDecodableString, options);

                // Get rotation and prepare matrix
                int rotation = getImageRotation(imgDecodableString);
                Matrix rotationMatrix = new Matrix();
                rotationMatrix.postRotate(rotation);

                // Create rotated bitmap
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitM, 0, 0, bitM.getWidth(), bitM.getHeight(), rotationMatrix, true);

                ivChosenImage.setImageBitmap(rotatedBitmap);

                tvSize.setText("Orientation: " + rotation + " ImageSizeBefore: " + beforeWidth + " " + beforeHeight + " " + imageByteSize + " ImageSizeAfter: " + options.outWidth + " " + options.outHeight + " " + options.outHeight * options.outWidth +
                        " Multiplyer: " + options.inSampleSize);


            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
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

                break;
            case R.id.btn_browse_pic:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(isExternalStorageWritable()) {
                    startActivityForResult(intent, RESULT_LOAD_IMG);
                }
                break;
        }
    }
}
