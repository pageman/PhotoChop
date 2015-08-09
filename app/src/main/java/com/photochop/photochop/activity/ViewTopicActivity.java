package com.photochop.photochop.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.photochop.photochop.AppConstants;
import com.photochop.photochop.R;
import com.photochop.photochop.adapter.CommentsListAdapter;
import com.photochop.photochop.adapter.FeedListAdapter;
import com.photochop.photochop.util.Util;
import com.photochop.photochop.util.WebServiceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alex on 8/9/15.
 */
public class ViewTopicActivity extends Activity {

    private final int CAPTURE_IMAGE_REQUEST = 0;
    private final int BROWSE_IMAGE_REQUEST = 1;
    private AlertDialog alertDialog;
    private String topicId;
    private String topicDesc;
    private String topicPoints;
    private String topicComments;
    private String topicImg;
    private ImageView ivTopicImg;
    private TextView tvTopicDesc;
    private TextView tvTopicPoints;
    private TextView tvTopicComments;


    public static final String TOPIC_ID = "TopicId";
    public static final String TOPIC_DESC = "TopicDesc";
    public static final String TOPIC_POINTS = "TopicPoints";
    public static final String TOPIC_COMMENTS = "TopicComments";
    public static final String TOPIC_IMG = "TopicIMG";
    private ListView listView;
    private CommentsListAdapter adapter;
    private ArrayList<HashMap<String,String>> list = new ArrayList<>();
    private WebServiceManager ws = new WebServiceManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_topic);

        Intent intent = getIntent();
        topicId = intent.getStringExtra(TOPIC_ID);
        topicDesc = intent.getStringExtra(TOPIC_DESC);
        topicPoints = intent.getStringExtra(TOPIC_POINTS);
        topicComments = intent.getStringExtra(TOPIC_COMMENTS);
        topicImg = intent.getStringExtra(TOPIC_IMG);

        ivTopicImg = (ImageView) findViewById(R.id.ivImageTPC);
        tvTopicDesc = (TextView) findViewById(R.id.tvCaptionTPC);
        tvTopicPoints = (TextView) findViewById(R.id.tvThumbsUpTPC);
        tvTopicComments = (TextView) findViewById(R.id.tvCommentsTPC);

        UrlImageViewHelper.setUrlDrawable(ivTopicImg,
                AppConstants.WS_BASE_URL + topicImg);

        tvTopicDesc.setText(topicDesc);
        tvTopicPoints.setText(topicPoints + " Points");
        tvTopicComments.setText(topicComments + " Comments");


        // ListView

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.commentList);

        adapter = new CommentsListAdapter(ViewTopicActivity.this, list);
        // Assign adapter to ListView
        listView.setAdapter(adapter);


        TelephonyManager telephonyManager = (TelephonyManager) ViewTopicActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
        String device_id = telephonyManager.getSimSerialNumber();
        String cmd = "getComments";

        JSONObject request = new JSONObject();
        try
        {
            request.put("deviceid", device_id);
            request.put("cmd", cmd);
            request.put("topicid", topicId);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        GetCommentsAsync task = new GetCommentsAsync();
        task.execute(request);

    }

    public void capture(View view) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, CAPTURE_IMAGE_REQUEST);
        alertDialog.dismiss();
    }

    public void browse(View view) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, BROWSE_IMAGE_REQUEST);
        alertDialog.dismiss();
    }

    public void selectImage(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.popup_select_image, null);
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Intent intent = new Intent(this, CreateCommentActivity.class);
        switch (requestCode) {
            case CAPTURE_IMAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    intent.putExtra(CreateCommentActivity.COMMENT_IMAGE, imageBitmap);
                }
                break;
            case BROWSE_IMAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    intent.putExtra(CreateCommentActivity.COMMENT_IMAGE, selectedImage);
                }
                break;
        }
        intent.putExtra(CreateCommentActivity.TOPIC_ID,topicId);
        startActivity(intent);
    }


    // Async Tasks Here....
    private class GetCommentsAsync extends AsyncTask<JSONObject, Void, JSONArray>
    {
        ProgressDialog dialog = new ProgressDialog(ViewTopicActivity.this);

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        protected JSONArray doInBackground(JSONObject... params)
        {
            return ws.getComments(params[0]);
        }

        protected void onPostExecute(JSONArray result)
        {
            dialog.dismiss();
            Util.log("GetFeedAsync", result.toString());

            for (int i = 0; i < result.length(); i++)
            {
                HashMap<String, String> cdr;
                try
                {
                    cdr = new HashMap<>();
                    JSONObject jsonItems = result.getJSONObject(i);

                    cdr.put("id", jsonItems.get("id").toString());
                    cdr.put("user", jsonItems.get("user").toString());
                    cdr.put("topicid", jsonItems.get("topicid").toString());
                    cdr.put("caption", jsonItems.get("caption").toString());
                    cdr.put("image", jsonItems.get("image").toString());
                    cdr.put("thumbsup", jsonItems.get("thumbsup").toString());
                    cdr.put("datecreated", jsonItems.get("datecreated").toString());
                    cdr.put("isregistered", jsonItems.get("isregisterd").toString());
                    list.add(cdr);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
//            parseResult(result);
        }


    }


}
