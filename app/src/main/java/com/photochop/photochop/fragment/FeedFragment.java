package com.photochop.photochop.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.photochop.photochop.AppConstants;
import com.photochop.photochop.R;
import com.photochop.photochop.adapter.FeedListAdapter;
import com.photochop.photochop.base.BaseFragment;
import com.photochop.photochop.util.Util;
import com.photochop.photochop.util.WebServiceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vaughn on 8/8/15.
 */
public class FeedFragment extends BaseFragment
{
    private ListView listView;
    private FeedListAdapter adapter;
    private WebServiceManager ws = new WebServiceManager();
    public ArrayList<HashMap<String, String>> list = new ArrayList<>();
    public static int mCategory;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        // Get ListView object from xml
        listView = (ListView) getView().findViewById(R.id.list);

        adapter = new FeedListAdapter(getActivity(), list);
        // Assign adapter to ListView
        listView.setAdapter(adapter);


        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String device_id = telephonyManager.getSimSerialNumber();


        String cmd;
        if (getCategory() == 1)
        {
            cmd = "getNewsFeed";
        } else if (getCategory() == 2)
        {
            cmd = "getPopular";

        } else
        {
            cmd = "getTrending";
        }


        JSONObject request = new JSONObject();
        try
        {
            request.put("deviceid", device_id);
            request.put("cmd", cmd);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
//        JSONObject result = new WebServiceManager().sendJson(request);
//        this.parseResult(result);


        GetFeedAsync task = new GetFeedAsync();
        task.execute(request);
    }

    private int getCategory()
    {
        return mCategory;
    }


    // Async Tasks Here....
    private class GetFeedAsync extends AsyncTask<JSONObject, Void, JSONArray>
    {
        ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        protected JSONArray doInBackground(JSONObject... params)
        {
            return ws.getFeed(params[0]);
        }

        protected void onPostExecute(JSONArray result)
        {
            dialog.dismiss();
            try
            {
                Util.log("GetFeedAsync", result.toString());
                for (int i = 0; i <= result.length(); i++)
                {
                    HashMap<String, String> cdr;
                    try
                    {
                        cdr = new HashMap<>();
                        JSONObject jsonItems = result.getJSONObject(i);

                        cdr.put("id", jsonItems.get("id").toString());
                        cdr.put("createdby", jsonItems.get("createdby").toString());
                        cdr.put("image", jsonItems.get("image").toString());
                        cdr.put("caption", jsonItems.get("caption").toString());
                        cdr.put("thumpsup", jsonItems.get("thumbsup").toString());
                        cdr.put("datecreated", jsonItems.get("datecreated").toString());
                        cdr.put("isregistered", jsonItems.get("isregistered").toString());
                        cdr.put("totalcomments", jsonItems.get("totalcomments").toString());
                        list.add(cdr);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
            catch (NullPointerException e)
            {
                Util.displayPopup(getActivity(), AppConstants.APP_NAME, AppConstants.CONNECTION_FAILED);
            }
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
    }
}
