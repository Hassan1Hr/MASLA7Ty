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
import android.support.v7.app.AppCompatActivity;
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
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.pojo.ApplicationURL;
import com.hassan.masla7ty.pojo.MyApplication;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG_CURRENT_LOC = "currentLoc";
    private static final int MEDIA_TYPE_IMAGE = 0;
    private EditText mNewsBody;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
    private Long time;
    private final OkHttpClient client = new OkHttpClient();
    MapLibActivity mapLibActivity = new MapLibActivity();
    private Button mPublishBtn;
    private ProgressBar progressBar;
    public static String filePath ;
    String mCurrentPhotoPath;
    double latitude;
    double longitude ;
    double radius;
    static File imageFile;
    String Username ;
    private ImageView image;
    private Uri fileUri;
    private final int TAKE_PICTURE = 0;
    private final int SELECT_PICTURE = 1;
    private static final String TAG_GALLARY = "gallery";
    private static final String TAG_CAMERA = "camera";
    JSONParser jsonParser = new JSONParser();
    private String ADD_URL =ApplicationURL.appDomain.concat("addPost.php");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        SharedPreferences sharedPref = MyApplication.getInstance().getSharedPreferences(MyApplication.UsernamePrefernce, Context.MODE_PRIVATE);
        Username= sharedPref.getString("username", "hassan@gmail.com");
        SharedPreferences locationSharedPref =getSharedPreferences(MyApplication.UserLocationPrefernce, Context.MODE_PRIVATE);

        latitude =locationSharedPref.getFloat("Latitude", (float) 27.185875);
        longitude =locationSharedPref.getFloat("Longitude", (float)31.168594 );
        radius =locationSharedPref.getFloat("radius", (float) 15);
        mNewsBody = (EditText) findViewById(R.id.news_box);
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

                Bitmap bitmap =  MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                image.setImageBitmap(bitmap);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                     imageFile = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "test.jpg");
                    imageFile.createNewFile();
                    FileOutputStream fo = new FileOutputStream(imageFile);
                    fo.write(bytes.toByteArray());
                    fo.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == TAKE_PICTURE) {

                filePath = fileUri.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                SetImage(bitmap);
                imageFile = new File(filePath);

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

    private class AddNewsTask extends AsyncTask<Void, Integer, String>
    {
        private ProgressDialog mProgressDialog;

        private JSONObject jsonObjectResult = null;

        private String error;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(HomeActivity.this,
                    "Uploading...", "Waiting while uploading the image", false, false);


        }



        @Override
        protected String doInBackground(Void... params)
        {
            return uploadFile();

        }
        private String uploadFile() {
            String responseString ;



            try {
                // Adding file data to http body
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                time = date.getTime();
                String dateString = dateFormat.format(date).toString();
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("postDate", dateString)
                        .addFormDataPart("postTime",time+"")
                        .addFormDataPart("postDescription", mNewsBody.getText().toString())
                        .addFormDataPart("latitude",latitude+"")
                        .addFormDataPart("longitude",longitude+"")
                        .addFormDataPart("radius", radius+"")
                        .addFormDataPart("creatorId", Username)
                        .addFormDataPart("image", "logo-square.png",
                                RequestBody.create(MEDIA_TYPE_PNG, imageFile))
                        .build();
                Request request = new Request.Builder()
                        .url(ADD_URL)
                        .post(requestBody)
                        .build();
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                responseString = response.body().string();



            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String responseString) {
            Log.e("Image Upload", "Response from server: " + responseString);
            mProgressDialog.dismiss();
            try {
                JSONObject result = new JSONObject(responseString);
                if (result.getInt("success") == 1) {
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
            }

            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
            super.onPostExecute(responseString);
        }

    }
}