package com.photochop.photochop.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.photochop.photochop.AppConstants;
import com.photochop.photochop.R;
import com.photochop.photochop.util.Util;
import com.photochop.photochop.util.WebServiceManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;


/**
 * Created by alex on 8/8/15.
 */
public class CreatePostActivity extends FragmentActivity {

    public static final String TAG = "CreatePostActivity";
    WebServiceManager ws = new WebServiceManager();

    private ImageView image;
    private AlertDialog alertDialog;
    private EditText etCaption;
    private String caption;
    private TextView captionCounter;
    private Button postButton;
    private final int CAPTURE_IMAGE_REQUEST = 0;
    private final int BROWSE_IMAGE_REQUEST = 1;
    private final int RESPONSE_STATUS_SUCCESS = 1;
    private final int RESPONSE_STATUS_FAIL = 0;
    private BitmapDrawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        image = (ImageView) findViewById(R.id.image);
        captionCounter = (TextView) findViewById(R.id.character_count);
        etCaption = (EditText) findViewById(R.id.caption);
        postButton = (Button) findViewById(R.id.post_topic);

        etCaption.addTextChangedListener(new CaptionTextWatcher());

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendImage(intent);
            }
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            image.setImageURI(imageUri);
        }
    }

    public void capture(View view) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, CAPTURE_IMAGE_REQUEST);
        alertDialog.dismiss();
    }
    public void browse(View view) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , BROWSE_IMAGE_REQUEST);
        alertDialog.dismiss();
    }

    public void postTopic(View view) {
        postButton.setEnabled(false);
//        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
//
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
//        byte[] bb = bos.toByteArray();
//
//
//        String device_id = telephonyManager.getSimSerialNumber();
//        String cmd = "addTopic";
//        String caption = this.caption.getText().toString();
//        String file = Base64.encodeToString(bb, Base64.DEFAULT);
//
//        JSONObject request = new JSONObject();
//        try {
//            request.put("deviceid", device_id);
//            request.put("cmd", cmd);
//            request.put("caption", caption);
//            request.put("file", file);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        SendJsonAsync task = new SendJsonAsync();
        task.execute();
    }

    private void parseResult(JSONObject json) {
        try{
            int status = json.getInt("status");
            String message = json.getString("message");
            if (status == RESPONSE_STATUS_SUCCESS) {
                Toast.makeText(this,message,Toast.LENGTH_LONG).show();
                new MainActivity().finish();
                Intent i = new Intent(CreatePostActivity.this, MainActivity.class);
                startActivity(i);
                this.finish();
            }else if(status == RESPONSE_STATUS_FAIL) {
                Toast.makeText(this,message,Toast.LENGTH_LONG).show();
                Util.displayPopup(CreatePostActivity.this, AppConstants.APP_NAME, AppConstants.CONNECTION_FAILED);

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        postButton.setEnabled(true);
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
        switch(requestCode) {
            case CAPTURE_IMAGE_REQUEST:
                if(resultCode == RESULT_OK){
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    image.setImageBitmap(imageBitmap);
                }
                break;
            case BROWSE_IMAGE_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    image.setImageURI(selectedImage);
                }
                break;
        }
    }

    private class CaptionTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            captionCounter.setText(etCaption.getText().length() + "/500");
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    // Async Tasks Here....
    private class SendJsonAsync extends AsyncTask<JSONObject, Void, JSONObject>
    {
        ProgressDialog dialog = new ProgressDialog(CreatePostActivity.this);
        String device_id;
        String cmd = "addTopic";
        String file;

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Please wait...");
            dialog.show();
            caption = etCaption.getText().toString();
            drawable = (BitmapDrawable) image.getDrawable();
        }

        protected JSONObject doInBackground(JSONObject... params)
        {
            Bitmap bitmap = drawable.getBitmap();

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
            byte[] bb = bos.toByteArray();


            device_id = telephonyManager.getSimSerialNumber();
            cmd = "addTopic";
            file = Base64.encodeToString(bb, Base64.DEFAULT);

            JSONObject request = new JSONObject();
            try {
                request.put("deviceid", device_id);
                request.put("cmd", cmd);
                request.put("caption", caption);
                request.put("file", file);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ws.addTopic(request);
        }

        protected void onPostExecute(JSONObject result)
        {
            dialog.dismiss();
            parseResult(result);
        }
    }

}
