package com.photochop.photochop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by alex on 8/9/15.
 */
public class ViewTopicActivity extends Activity {

    private final int CAPTURE_IMAGE_REQUEST = 0;
    private final int BROWSE_IMAGE_REQUEST = 1;
    private AlertDialog alertDialog;
    private String topicId;
    public static final String TOPIC_ID = "TopicId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_topic);

        Intent intent = getIntent();
        topicId = intent.getStringExtra(TOPIC_ID);

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


}
