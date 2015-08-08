package com.photochop.photochop.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.photochop.photochop.AppConstants;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Vaughn on 8/8/15.
 */

public class ImageManager
{
    private static boolean downloadImageSuccess = false;
    private static Context mContext;
    public static String TAG = "WebServiceManager";

    public ImageManager(Context context)
    {
        mContext = context;
    }


    public static void downloadPhoto(String img_url)
    {
        downloadImageSuccess = false;
        // This is for downloading venue photos in the venue list view
        try
        {
            if ("-".equals(img_url))
            {
                return;
            }
            URL url = new URL(img_url);
            File cacheDir = new File(AppConstants.CACHED_IMGS_DIR);
            // create directory for cached images if it doesn't exist
            if (!cacheDir.exists())
            {
                cacheDir.mkdir();
                Log.d("Cache directory doest exist ",
                        " create directory for cached images if it doesn't exist");
            }

            File file = new File(AppConstants.CACHED_IMGS_DIR
                    + getBaseFilename(url.toString()));
            URLConnection ucon = url.openConnection();
            ucon.setReadTimeout(30000);
            ucon.setConnectTimeout(30000);
            ucon.setUseCaches(true);
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1)
            {
                baf.append((byte) current);
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            downloadImageSuccess = true;
            Log.d("Image Manager", file.getPath() + " cached");
        } catch (IOException | NetworkOnMainThreadException e)
        {
            Log.d("ImageManager", "Error: " + e);
        }
    }

    public static boolean isDownloadImageSuccess()
    {
        return downloadImageSuccess;
    }


    private static String getBaseFilename(String URL)
    {
        try
        {
            int fn_index = URL.lastIndexOf('/');
            String fn = URL.substring(fn_index + 1, URL.length());
            return fn;
        } catch (Exception e)
        {
            return "";
        }
    }


    public static Bitmap getBitmap(String img_url,
                                   Integer reqWidth, Integer reqHeight)
            throws FileNotFoundException
    {
        Log.e("getBitmap", img_url);
        Bitmap bitmap = null;
        File file = new File(AppConstants.CACHED_IMGS_DIR
                + getBaseFilename(img_url));

        Log.e("getBitmap", file.getAbsolutePath());
        if (file.exists() && file.length() == 0)
        {
            file.delete();
            Log.e("getBitmap", "tangina naman");
        }
        if (!file.exists() && !"-".equals(img_url))
        {
            throw new FileNotFoundException("Something went wrong");
        }


        // Bitmap bitmap = BitmapFactory.decodeStream(new
        // FileInputStream(file));

        if (reqWidth != null && reqHeight != null)
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            options.inSampleSize = ImageManager.calculateInSampleSize(options,
                    reqWidth, reqHeight);
            options.inTargetDensity = 160;
            options.inDensity = 160;
            options.inScaled = true;
            options.inJustDecodeBounds = false;
//            bitmap = BitmapFactory.decodeFile(filePath, options);
//            bitmap = BitmapFactory.decodeFile(filePath, options);
            bitmap = BitmapFactory.decodeFile(new File(mContext.getFilesDir(), getBaseFilename(img_url)).getAbsolutePath(), options);

            // bitmap = BitmapFactory.decodeResource(context.getResources(),
            // R.drawable.corp_nestle, options);
        } else
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inTargetDensity = 160;
            options.inDensity = 160;
            options.inScaled = true;
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            Log.e("getBitmap", "tangina2");
        }

        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public void log(String msg)
    {
        Log.e(TAG, msg);
    }

    public void logURL(String msg)
    {
        Log.e(TAG, "URL: " + msg);
    }

    public void logParam(String msg)
    {
        Log.e(TAG, "Params: " + msg);
    }


}
