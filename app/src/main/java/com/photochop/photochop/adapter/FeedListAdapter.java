package com.photochop.photochop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.photochop.photochop.AppConstants;
import com.photochop.photochop.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vaughn on 8/8/15.
 */
public class FeedListAdapter extends BaseAdapter
{


    private Context mContext;
    private ArrayList<HashMap<String, String>> mList;


    public FeedListAdapter(Context context, ArrayList<HashMap<String, String>> list)
    {
        super();
        mContext = context;
        mList = list;
    }

    public int getCount()
    {
        // TODO Auto-generated method stub
        //return VenueListViewActivity.VENUES.length;
        return mList != null ? mList.size() : 0;

//        return 25;
    }


    public String getItem(int position)
    {
        // TODO Auto-generated method stub
        return "detail " + position;
    }


    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v;
        if (convertView == null)
        {
            LayoutInflater li = LayoutInflater.from(mContext);
            v = li.inflate(R.layout.fragment_feed_row, null);
        } else
        {
            v = convertView;
        }

        HashMap item = mList.get(position);

        TextView tvCaption = (TextView) v.findViewById(R.id.tvCaption);
        TextView tvThumbsUp = (TextView) v.findViewById(R.id.tvThumbsUp);
        TextView tvComments = (TextView) v.findViewById(R.id.tvComments);

        UrlImageViewHelper.setUrlDrawable((ImageView) v.findViewById(R.id.ivImage),
                AppConstants.WS_BASE_URL + item.get("image").toString());

        tvCaption.setText(item.get("caption").toString());
        tvThumbsUp.setText(item.get("thumpsup").toString() + " Points");
        tvComments.setText(item.get("totalcomments").toString() + " Comments");

        return v;
    }


}
