package com.hassan.masla7ty.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.pojo.AndroidMultiPartEntity;
import com.hassan.masla7ty.pojo.ApplicationURL;
import com.hassan.masla7ty.pojo.MyApplication;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class HomeActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String TAG_CURRENT_LOC = "currentLoc";
    private static final int MEDIA_TYPE_IMAGE = 0;
    private EditText mNewsBody;
    private String userName;
    private Long time;
    private String UploadImage;
    MapLibActivity mapLibActivity = new MapLibActivity();
    private Button mPublishBtn;
    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    private ImageView imgPreview;
    String mCurrentPhotoPath;
    double latitude;
    double longitude ;
    double radius;
    String Username ;
    long totalSize = 0;
    private ImageView image;
    private Uri fileUri;
    private final int TAKE_PICTURE = 0;
    private final int SELECT_PICTURE = 1;
    private ProgressDialog mProgressDialog;

    private JSONObject jsonObjectResult = null;


    private static final String TAG_GALLARY = "gallery";
    private static final String TAG_CAMERA = "camera";
    JSONParser jsonParser = new JSONParser();
    private String ADD_URL =
            ApplicationURL.appDomain+"addPost.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(LoginActivity.UsernamePrefernce, Context.MODE_PRIVATE);
        Username= sharedPref.getString("username", "hassan@gmail.com");
        SharedPreferences locationSharedPref =getSharedPreferences(MainActivity.UserLocationPrefernce, Context.MODE_PRIVATE);
        UploadImage = new String("");
        latitude =locationSharedPref.getFloat("Latitude", (float) 27.185875);
        longitude =locationSharedPref.getFloat("Longitude", (float)31.168594 );
        radius =locationSharedPref.getFloat("radius", (float) 15);
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
            uploadImage();
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
            startActivityForResult(mIntent,132);
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
                try {
                    //Getting the Bitmap from Gallery
                Bitmap bitmap =  MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    image.setImageBitmap(bitmap);
                    UploadImage = getStringImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == TAKE_PICTURE) {

                filePath = fileUri.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                image.setImageBitmap(bitmap);
                UploadImage = getStringImage(bitmap);
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


    private void uploadImage(){
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(10);
        // updating progress bar value

        // updating percentage value
      //  txtPercentage.setText(String.valueOf(progress[0]) + "%");
        //Showing the progress dialog
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        try {
                            JSONObject result = new JSONObject(responseString);
                            if (result.getInt("success") == 1) {
                                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                                finish();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        //Showing toast
                        Toast.makeText(HomeActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                time = date.getTime();
                String dateString = dateFormat.format(date).toString();
                params.put("postDate",dateString);
                params.put("postTime",time+"");
                params.put("postDescription", mNewsBody.getText().toString());
                params.put("latitude",latitude+"");
                params.put("longitude",longitude+"");
                params.put("radius", radius+"");
                params.put("creatorId", userName);
                if(UploadImage.equals("")){
                    return params;
                }else
                params.put("image", UploadImage);


                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}