package com.photochop.photochop;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Created by Vaughn on 8/8/15.
 */
public class AppConstants {


    public static final String APP_NAME = "PhotoChop";
    public static final String APP_DESC = "A Small Photo Community!!";
    public static final String WS_BASE_URL = "http://photochop.cloudapp.net/index.php";



    // Connection
    public static final String NO_CONNECTION = "Internet connection is needed to connect to the server.";
    public static final String CONNECTION_FAILED = "Failed to connect to the server. Try Again later!.";

    // Img dir
//    public static final String CACHED_IMGS_DIR = "Failed to connect to the server. Try Again later!.";
    public static final String CACHED_IMGS_DIR = "/data/data/com.photochop.photochop/images/";


}
