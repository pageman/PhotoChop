package com.photochop.photochop.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.photochop.photochop.R;
import com.photochop.photochop.adapter.FeedListAdapter;
import com.photochop.photochop.base.BaseFragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get ListView object from xml
        listView = (ListView) getView().findViewById(R.id.list);

        adapter = new FeedListAdapter(getActivity(), list);
        // Assign adapter to ListView
        listView.setAdapter(adapter);

        SampleAsync task = new SampleAsync();
        task.execute();
    }


    private class SampleAsync extends AsyncTask<String, Void, JSONObject>
    {
        protected JSONObject doInBackground(String... params) {
//            return ws.getCallHistory(params[0]);
            return new JSONObject();
        }

        protected void onPostExecute(JSONObject result) {
            Log.e("joint mango and bacon", result.toString());

            Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_SHORT).show();
            try {
                HashMap<String, String> cdr;

                JSONArray jsonArray = result.getJSONArray("call_history");
                for (int n = 0; n < jsonArray.length(); n++) {
                    cdr = new HashMap<>();
                    JSONObject jsonItems = jsonArray.getJSONObject(n);

                    cdr.put("to_destination", jsonItems.get("to_destination").toString());
                    cdr.put("call_price", jsonItems.get("call_price").toString());
                    cdr.put("call_duration", jsonItems.get("call_duration").toString());
                    cdr.put("call_date", jsonItems.get("call_date").toString());
                    cdr.put("call_start", jsonItems.get("call_start").toString());
                    cdr.put("called_country_code", jsonItems.get("called_country_code").toString());
                    list.add(cdr);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();

        }
    }
}
