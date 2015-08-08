package com.photochop.photochop.util;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Vaughn on 8/8/15.
 */
public class Util
{

    public static boolean checkConnectionStatus(Context c)
    {
        final ConnectivityManager connMgr = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean connected = false;
        if (wifi.isConnected())
        {
            Log.d("Network status", "Connected to Wifi");
            connected = true;
        } else if (mobile.isConnected())
        {
        } else
        {
            // Toast.makeText(this, "No Network " , Toast.LENGTH_LONG).show();
            Log.d("Network status", "No internet connection available");
            connected = false;
        }
        return connected;
    }

    public static void displayPopup(Context context, String title,
                                    String message)
    {
        Builder alertbox = new Builder(context);
        alertbox.setTitle(title);
        alertbox.setMessage(message);
        alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface arg0, int arg1)
            {
            }
        });
        alertbox.show();
    }

    public static void displayPopup(final Context context, String title,
                                    String message, final Intent intent)
    {
        Builder alertbox = new Builder(context);
        alertbox.setTitle(title);
        alertbox.setMessage(message);

        alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface arg0, int arg1)
            {
                Activity activity = (Activity) context;
                context.startActivity(intent);
                activity.finish();
            }
        });
        alertbox.show();
    }

    public static void toast(Context context, String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
    public static void log(String tag, String msg)
    {
        Log.e(tag, msg);
    }
}
