package com.photochop.photochop.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.photochop.photochop.R;
import com.photochop.photochop.base.BaseFragmentActivity;
import com.photochop.photochop.util.ImageManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageViewerActivity extends BaseFragmentActivity
{

    ImageManager im;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        im = new ImageManager(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadImageTask task = new DownloadImageTask();
        task.execute("http://www.cdc.gov/importation/images/dog2.jpg");

    }


    private String saveToInternalSorage(Bitmap bitmapImage)
    {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try
        {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String path)
    {

        try
        {
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = (ImageView) findViewById(R.id.ivImage);
            img.setImageBitmap(b);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Log.e("Error", "File not found");
        }

    }

    public static Bitmap getBitmapFromURL(String src)
    {
        try
        {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e)
        {
            // Log exception
            return null;
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog = new ProgressDialog(ImageViewerActivity.this);

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        protected String doInBackground(String... imgUrl)
        {
            String imageUrl = imgUrl[0];
            String message = "failed";
            try
            {
                im.downloadPhoto(imageUrl);
                if (ImageManager.isDownloadImageSuccess())
                    message = "success";

            }
            catch (Exception | OutOfMemoryError e)
            {
                e.printStackTrace();
                Log.i("NO PHOTO", "NO PHOTO");
            }

            return message + ": " + imageUrl;
        }

        protected void onPostExecute(String message)
        {
            dialog.dismiss();
            Log.d("Downlading image task completed", "Download " + message);


            ImageView img = (ImageView) findViewById(R.id.ivImage);

            try
            {
                Bitmap bitmap = im.getBitmap("http://www.cdc.gov/importation/images/dog2.jpg", 150, 150);

                img.setImageBitmap(bitmap);
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

}
