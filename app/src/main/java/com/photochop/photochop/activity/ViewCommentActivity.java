package com.photochop.photochop.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.photochop.photochop.R;

/**
 * Created by alex on 8/9/15.
 */
public class ViewCommentActivity extends Activity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comment);

        imageView = (ImageView) findViewById(R.id.ivImage);

    }

}
