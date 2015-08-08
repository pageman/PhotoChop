package com.photochop.photochop.base;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.photochop.photochop.R;

import static android.view.WindowManager.*;

/**
 * Created by Vaughn on 8/8/15.
 */
public class BaseActivity extends Activity
{
    public static int mainColor= 0xffD32F2F;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();

    }
}
