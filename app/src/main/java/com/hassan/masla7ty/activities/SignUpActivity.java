package com.hassan.masla7ty.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hassan.masla7ty.R;
import com.hassan.masla7ty.MainClasses.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends ActionBarActivity {
    protected EditText firstName;
    protected EditText lastName;
    protected EditText mEmail;
    protected EditText password;
    protected EditText gender;
    protected EditText age;
    protected EditText city;
    protected EditText mobile;
    protected Button mSignUpButton;
    protected Button mCancelButton;
    private JSONParser jsonParser = new JSONParser();
    private String REGISTER_URL =
            "http://masla7tyfinal.esy.es//app/userSignup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        mEmail = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        gender = (EditText) findViewById(R.id.gender);
        age = (EditText) findViewById(R.id.age);
        city = (EditText) findViewById(R.id.city);
        mobile = (EditText) findViewById(R.id.mobile);

        mCancelButton = (Button) findViewById(R.id.cancelButton);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mSignUpButton = (Button) findViewById(R.id.signupButton);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = firstName.getText().toString();
                String lastname = lastName.getText().toString();
                String username = mEmail.getText().toString();
                String userpassword = password.getText().toString();
                String usergender = gender.getText().toString();
                String userage = age.getText().toString();
                String usercity = city.getText().toString();
                String usermobile = mobile.getText().toString();

                firstname = firstname.trim();
                lastname = lastname.trim();
                username = username.trim();
                userpassword = userpassword.trim();
                usergender =usergender.trim();
                userage =userage.trim();
                usercity = usercity.trim();
                usermobile =usermobile.trim();



                if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || userpassword.isEmpty()||usergender.isEmpty() || userage.isEmpty() || usercity.isEmpty() || usermobile.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // create the new user!
                    new RegisterUserTask(firstname, lastname,username,userpassword,usergender,userage,usercity,usermobile).execute();


                }
            }
        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class RegisterUserTask extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog mProgressDialog;

        private JSONObject jsonObjectResult = null;

        private String firstname;
        private String lastname;
        private String username;
        private String password;
        private String gender;
        private String age;
        private String city;
        private String mobile;


        private String error;

        private RegisterUserTask(String firstname, String lastname,String username,String userpassword,String usergender,String userage,String usercity,String usermobile)
        {
            this.firstname=firstname;
            this.lastname=lastname;
            this.username=username;
            password =userpassword;
            gender =usergender;
            age =userage;
            city = usercity;
            mobile = usermobile;

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(SignUpActivity.this,
                    "Processing...", "Creating new user", false, false);
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("firstName", firstname));
            pairs.add(new BasicNameValuePair("lastName", lastname));
            pairs.add(new BasicNameValuePair("userName", username));
            pairs.add(new BasicNameValuePair("password", password));
            pairs.add(new BasicNameValuePair("gender", gender));
            pairs.add(new BasicNameValuePair("age", age));
            pairs.add(new BasicNameValuePair("city", city));
            pairs.add(new BasicNameValuePair("mobile", mobile));

            jsonObjectResult = jsonParser.makeHttpRequest(REGISTER_URL, pairs);
            if (jsonObjectResult == null)
            {
                error = "Error int the connection";
                return false;
            }

            try
            {
                if (jsonObjectResult.getInt("success") == 1)
                    return true;
                else
                    error = "error in php";

            }
            catch (Exception ex)
            {

            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);
            mProgressDialog.dismiss();
            if (aBoolean)
            {
                Intent mIntent = new Intent(SignUpActivity.this, MainActivity.class);
                Toast.makeText(getApplicationContext(), "Success ", Toast.LENGTH_LONG).show();
                startActivity(mIntent);
            }
            else
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
        }
    }
}
