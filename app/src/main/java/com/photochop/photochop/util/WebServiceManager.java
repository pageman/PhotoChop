package com.photochop.photochop.util;

import android.os.Looper;
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
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.InputStream;
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
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("param1", param2));
            nameValuePairs.add(new BasicNameValuePair("param2", param2));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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

    public JSONObject sendJson(final JSONObject jsonObject) {

        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
        HttpResponse response;

        try {
            HttpPost post = new HttpPost(AppConstants.WS_BASE_URL);
            post.setHeader(CoreProtocolPNames.USER_AGENT,"Mozilla/5.0 (Windows NT 6.2; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0");
            StringEntity se = new StringEntity(jsonObject.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            response = client.execute(post);

            Log.e("Pek",String.valueOf(response));
            if(response!=null){
                String _response = EntityUtils.toString(response.getEntity());

                JSONObject ret = new JSONObject(_response);
                return ret;
            }
        } catch(Exception e) {
            Log.e("Fuck",e.getStackTrace().toString());
        }
        return null;
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
