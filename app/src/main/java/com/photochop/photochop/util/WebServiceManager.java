package com.photochop.photochop.util;

import android.util.Log;

import com.photochop.photochop.AppConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaughn on 8/8/15.
 */

public class WebServiceManager
{
    public static String TAG = "WebServiceManager";


    public String sampleGetWithParameter(String params)
    {
        // HTTP GET Params
        String param = "/" + params;

        // URL
        String prefix = "/call/callback";
        String url = AppConstants.WS_BASE_URL + prefix + "/status";
        url = url + param;
        logURL(url);

        // Request Parameters
        logParam(param);


        // HTTP Request
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 3000);
        HttpConnectionParams.setSoTimeout(client.getParams(), 3000);
        HttpResponse response;
        HttpGet httpget = new HttpGet(url);
        try
        {
            response = client.execute(httpget);
            HttpEntity resEntity = response.getEntity();
            String _response = EntityUtils.toString(resEntity); // content will be consume only once


            JSONObject jsonObject = new JSONObject(_response);
            return jsonObject.getString("");
        } catch (Exception e)
        {
            e.printStackTrace();
            return AppConstants.CONNECTION_FAILED;
        }
    }

    public JSONObject samplePost(String param1, String param2)
    {

        // URL
        String prefix = "";
        String url = AppConstants.WS_BASE_URL + prefix;
        logURL(url);

        // HTTP Request
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 3000);
        HttpConnectionParams.setSoTimeout(client.getParams(), 3000);
        HttpResponse response;
        HttpPost httppost = new HttpPost(url);
        try
        {
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//            nameValuePairs.add(new BasicNameValuePair("param1", param2));
//            nameValuePairs.add(new BasicNameValuePair("param2", param2));
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            JSONObject a  = new JSONObject("{hello1: hi, hello2: hey}");
            String jsonString = a.toString().substring(1, a.toString().length() - 1);


            httppost.setEntity(new StringEntity(jsonString, "UTF8"));
            httppost.setHeader("Content-type", "application/json");

            // Request Parameters
            logParam(param1);
            logParam(param2);

            response = client.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            String _response = EntityUtils.toString(resEntity); // content will be consume only once

            JSONObject jsonObject = new JSONObject(_response);
            return jsonObject;
        } catch (Exception e)
        {
            e.printStackTrace();
            return  new JSONObject();
        }
    }


    public void log(String msg)
    {
        Log.e(TAG, msg);
    }

    public void logURL(String msg)
    {
        Log.e(TAG, "URL: " + msg);
    }

    public void logParam(String msg)
    {
        Log.e(TAG, "Params: " + msg);
    }


}
