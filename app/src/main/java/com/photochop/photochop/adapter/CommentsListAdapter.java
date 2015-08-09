package com.photochop.photochop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.photochop.photochop.AppConstants;
import com.photochop.photochop.R;
import com.photochop.photochop.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vaughn on 8/8/15.
 */
public class CommentsListAdapter extends BaseAdapter
{


    private Context mContext;
    private ArrayList<HashMap<String, String>> mList;


    public CommentsListAdapter(Context context, ArrayList<HashMap<String, String>> list)
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
            v = li.inflate(R.layout.activity_view_topic_comment_row, null);
        } else
        {
            v = convertView;
        }

        HashMap item = mList.get(position);

        TextView tvCaption = (TextView) v.findViewById(R.id.tvCaption);
        TextView tvThumbsUp = (TextView) v.findViewById(R.id.tvThumbsUp);
        TextView tvComments = (TextView) v.findViewById(R.id.tvComments);
        ImageView ivImageView = (ImageView) v.findViewById(R.id.ivImage);

        UrlImageViewHelper.setUrlDrawable(ivImageView,
                AppConstants.WS_BASE_URL + item.get("image").toString());

        ivImageView.setContentDescription(item.get("id").toString());


        Util.toast(mContext, item.get("caption").toString());
        Util.toast(mContext, item.get("thumbsup").toString());
        tvThumbsUp.setText(item.get("thumbsup").toString() + " Points");

        tvCaption.setText(item.get("caption").toString());

        return v;
    }


}
