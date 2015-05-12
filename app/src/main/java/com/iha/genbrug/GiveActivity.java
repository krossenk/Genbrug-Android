package com.iha.genbrug;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class GiveActivity extends Activity implements View.OnClickListener {

    ImageButton btnCamPic;
    ImageButton btnBrowsePic;
    View relImageBackground;
    TextView tvSize;

    ImageView imgView;
    private static int RESULT_LOAD_IMG = 1;
    private static long MAX_IMAGE_SIZE = 150000;
    private String imgDecodableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give);

        btnCamPic = (ImageButton) findViewById(R.id.btn_cam_pic);
        btnBrowsePic = (ImageButton) findViewById(R.id.btn_browse_pic);
        relImageBackground = findViewById(R.id.rel_image_background);
        imgView = (ImageView) findViewById(R.id.image_view);
        tvSize = (TextView) findViewById(R.id.tv_image_size);

        btnCamPic.setOnClickListener(this);
        btnBrowsePic.setOnClickListener(this);
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

                // ImageView after decoding the String
                Bitmap bitM = BitmapFactory.decodeFile(imgDecodableString, options);
                imgView.setImageBitmap(bitM);
                tvSize.setText("ImageSizeBefore: " + beforeWidth + " " + beforeHeight + " " + imageByteSize + " ImageSizeAfter: " + options.outWidth + " " + options.outHeight + " " + options.outHeight * options.outWidth +
                        " Multiplyer: " + options.inSampleSize);


                //relImageBackground.setBackground(new BitmapDrawable(getResources(), bitM));

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
