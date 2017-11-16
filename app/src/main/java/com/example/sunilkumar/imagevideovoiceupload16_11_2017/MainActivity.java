package com.example.sunilkumar.imagevideovoiceupload16_11_2017;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int getResponseFromCamera = 1;
    ImageView captureImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captureImage = (ImageView) findViewById(R.id.captureImage_id);
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked Capture Image", Toast.LENGTH_SHORT).show();

                checkInternalStorageMemory();
                checkSdCardMemory();


                Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
                Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();

                if(isSDPresent)
                {
                    // yes SD-card is present
                    Log.e("sdcardstaus","yes");
                }
                else
                {
                    Log.e("sdcardstaus","No");
                }

                File file = new File("/mnt/extSdCard");
                try
                {
                    File list[] = file.listFiles();
                    Toast.makeText(getApplicationContext(), "Yes sdcard is mounted, file count "+list.length, Toast.LENGTH_LONG).show();
                }
                catch(NullPointerException o)
                {
                    Toast.makeText(getApplicationContext(), "Sorry no sdcard is mounted:", Toast.LENGTH_LONG).show();
                }





                File storage_directory = new File("/storage");
                File storage;

                for (int i = 0; i < storage_directory.listFiles().length; i++) {

                    storage = storage_directory.listFiles()[i];


                    if (storage.getAbsolutePath().contains("emulated")) {
                        continue;
                    }

                    if (storage.getAbsolutePath().equals("/storage/self")) {
                        continue;
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (Environment.getExternalStorageState(storage).equals(Environment.MEDIA_MOUNTED)) {

                            if (Environment.isExternalStorageEmulated(storage) == false) {

                                final File finalStorage = storage;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("thirdstate", "External SD Card exists. Path: " + finalStorage.getAbsolutePath());

                                    }
                                });
                            }
                        }

                        else {
                            Log.d("asdf", "No external Storage detected.");
                        }
                    }
                }









              //  megabytesAvailable();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File fileDir = new File(Environment.getExternalStorageDirectory() + "/suneel");
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }

                String imagePath = Environment.getExternalStorageDirectory() + "/suneel/"
                        + System.currentTimeMillis() + ".jpg";
               File carmeraFile = new File(imagePath);
               Uri imageCarmeraUri = Uri.fromFile(carmeraFile);

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        imageCarmeraUri);

                startActivityForResult(cameraIntent, getResponseFromCamera);
            }
        });

    }

    private long checkInternalStorageMemory() {

        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize, availableBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        }
        Log.e("space",""+availableBlocks*blockSize);
        return availableBlocks * blockSize;
    }


    private void checkSdCardMemory() {

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long)stat.getBlockSize() *(long)stat.getBlockCount();
        long megAvailable = bytesAvailable / 1048576;
        Log.e("space",""+megAvailable);
        System.out.println("Megs :"+megAvailable);

    }


    public static float megabytesAvailable(File f) {
        StatFs stat = new StatFs(f.getPath());
        long bytesAvailable = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
            bytesAvailable = (long) stat.getBlockSizeLong() * (long) stat.getAvailableBlocksLong();
        else
            bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
        return bytesAvailable / (1024.f * 1024.f);
    }






    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == getResponseFromCamera && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //captureImage.setImageBitmap(photo);
        }
    }

}
