package com.photochop.photochop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.photochop.photochop.R;
import com.photochop.photochop.base.BaseTabActivity;


public class TabhostActivity extends BaseTabActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabhost);


        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, HotActivity.class);
        spec = tabHost.newTabSpec("First").setIndicator("First")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, PopularActivity.class);
        spec = tabHost.newTabSpec("Second").setIndicator("Second")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, TrendingActivity.class);
        spec = tabHost.newTabSpec("Third").setIndicator("Third")
                .setContent(intent);
        tabHost.addTab(spec);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
