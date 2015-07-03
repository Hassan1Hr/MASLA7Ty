package com.hassan.masla7ty.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.pojo.AndroidMultiPartEntity;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;




/**
 * Created by sherif on 23/04/15.
 */
public class HomeActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String TAG_CURRENT_LOC = "currentLoc";
    private static final int MEDIA_TYPE_IMAGE = 0;
    private EditText mNewsBody;
    private String userName;
    private Long time;
    MapLibActivity mapLibActivity = new MapLibActivity();
    private Button mPublishBtn;
    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    private ImageView imgPreview;
    String mCurrentPhotoPath;

    long totalSize = 0;
    private ImageView image;
    private Uri fileUri;
    private final int TAKE_PICTURE = 0;
    private final int SELECT_PICTURE = 1;
    private static final String TAG_GALLARY = "gallery";
    private static final String TAG_CAMERA = "camera";
    JSONParser jsonParser = new JSONParser();
    private String ADD_URL =
            "http://masla7ty.esy.es/app/post.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mNewsBody = (EditText) findViewById(R.id.news_box);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPublishBtn = (Button) findViewById(R.id.publish_btn);
        image = (ImageView) findViewById(R.id.photoTaken);
        mPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attempAdding();
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_location_on_black_24dp);
        ImageView iconCurrentLoc = new ImageView(this);
        ImageView iconCamera = new ImageView(this);
        ImageView iconGallary = new ImageView(this);
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();
        iconCurrentLoc.setImageResource(R.drawable.ic_my_location_black_24dp);
        iconCamera.setImageResource(R.drawable.ic_photo_camera_black_24dp);
        iconGallary.setImageResource(R.drawable.ic_photo_black_24dp);
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        //itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_my_location_black_24dp));
        SubActionButton buttonCurrentLoc = itemBuilder.setContentView(iconCurrentLoc).build();
        SubActionButton btnCamera = itemBuilder.setContentView(iconCamera).build();
        SubActionButton btnGallary = itemBuilder.setContentView(iconGallary).build();
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonCurrentLoc)
                .addSubActionView(btnGallary)
                .addSubActionView(btnCamera)
                .attachTo(actionButton)
                .build();
        buttonCurrentLoc.setOnClickListener(this);

        btnCamera.setOnClickListener(this);
        btnGallary.setOnClickListener(this);

        buttonCurrentLoc.setTag(TAG_CURRENT_LOC);
        btnGallary.setTag(TAG_GALLARY);
        btnCamera.setTag(TAG_CAMERA);
    }

    private void attempAdding()
    {
        if (!mNewsBody.getText().toString().equals(""))
        {
            new AddNewsTask().execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "All fields are requires", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void drawRandomMarkers() {
        MarkerOptions options1 = new MarkerOptions()
                .title("marker")
                .position(new LatLng((52), Math.random() * 13));
    }
    @Override
    public void onClick(View v) {
        if (v.getTag().equals(TAG_CURRENT_LOC)) {
            Intent mIntent = new Intent(HomeActivity.this, MapLibActivity.class);
            startActivity(mIntent);
        }else if (v.getTag().equals(TAG_CAMERA)){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
               Toast.makeText(getApplicationContext(),"error to create file for image",Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                fileUri =  Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, TAKE_PICTURE);
            }
          }
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                filePath = getPath(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                SetImage(bitmap);
            } else if (requestCode == TAKE_PICTURE) {

                filePath = fileUri.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                SetImage(bitmap);

            }

        }
    }





    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private void SetImage(Bitmap image) {
        this.image.setImageBitmap(image);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        galleryAddPic();
        return image;

    }
    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();

    }
    private class AddNewsTask extends AsyncTask<Void, Integer, String>
    {
        private ProgressDialog mProgressDialog;

        private JSONObject jsonObjectResult = null;

        private String error;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressBar.setProgress(0);

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            //super.onProgressUpdate(progress);
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params)
        {
            return uploadFile();

        }
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(ADD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                File sourceFile = new File(filePath);

                // Adding file data to http body
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                time = date.getTime();
                String dateString = dateFormat.format(date).toString();
                entity.addPart("postDate",new StringBody(dateString));
                entity.addPart("postTime",new StringBody(time+""));
                entity.addPart("postDescription", new StringBody(mNewsBody.getText().toString()));
                entity.addPart("latitude",new StringBody (27.193054+""));//(MapLibActivity.latitude)+""));
                entity.addPart("longitude", new StringBody(27.209184+""));//(MapLibActivity.longitude)+""));
               // entity.addPart("radius", new StringBody((MapLibActivity.mRadius)+""));
                entity.addPart("creatorId",new StringBody(LoginActivity.getUsername()));
                entity.addPart("image", new FileBody(sourceFile));


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity httpEntity = response.getEntity();

                if (httpEntity != null)
                {
                    responseString = EntityUtils.toString(httpEntity);


                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String responseString) {
            Log.e("Image Upload", "Response from server: " + responseString);
            try {
                JSONObject result = new JSONObject(responseString.substring(responseString.indexOf("{"), responseString.lastIndexOf("}") + 1));
                if (result.getInt("success") == 1) {
                    Toast.makeText(getApplicationContext(), "sucess", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            super.onPostExecute(responseString);
        }

    }
}