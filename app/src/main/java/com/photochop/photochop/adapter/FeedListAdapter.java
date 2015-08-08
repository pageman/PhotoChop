package com.photochop.photochop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.photochop.photochop.R;

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

        // begin paste
        HashMap item = mList.get(position);

        TextView tvNumber = (TextView) v.findViewById(R.id.tvNumber);
        TextView tvDate = (TextView) v.findViewById(R.id.tvDate);
        TextView tvTime = (TextView) v.findViewById(R.id.tvTime);
        TextView tvDuration = (TextView) v.findViewById(R.id.tvDuration);
        TextView tvPrice = (TextView) v.findViewById(R.id.tvPrice);

        tvNumber.setText(item.get("to_destination").toString());
        tvDate.setText(item.get("call_date").toString());
        tvTime.setText(item.get("call_start").toString());
        tvDuration.setText(item.get("call_duration").toString());
        tvPrice.setText("$" + item.get("call_price").toString());
//
//        tvNumber.setText("hi");
//        tvDate.setText("hello");
//        tvTime.setText("bonbon");
//        tvDuration.setText("me");
//        tvPrice.setText("armads ");



        return v;
    }

}
