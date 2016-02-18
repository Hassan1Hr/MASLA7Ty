
package com.hassan.masla7ty.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.pojo.ApplicationURL;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    public static final String UsernamePrefernce = "username" ;
    private EditText mUsernameET;
    private EditText mPasswordET;
    protected TextView mSignUpTextView;
    protected TextView mSignUpEnterprise;
    private Button mSigninBtn;
    private static String username;


    private JSONParser jsonParser = new JSONParser();

    private String LOGIN_URL = ApplicationURL.appDomain+"signin.php";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        SharedPreferences sharedPref =getSharedPreferences(LoginActivity.UsernamePrefernce, Context.MODE_PRIVATE);
        String Usernameprefrence= sharedPref.getString("username", "notfound");
        String passwordprefernce= sharedPref.getString("password", "notfound");
        if(Usernameprefrence !="notfound" && passwordprefernce != "notfound")
        {
            Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mIntent);
            finish();

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mUsernameET = (EditText) findViewById(R.id.usernameET);
        mUsernameET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_ACTION_DONE) {
                    mPasswordET.requestFocus();
                }
                return false;
            }
        });

        mPasswordET = (EditText) findViewById(R.id.passowrdET);
        mPasswordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_ACTION_DONE) {
                    attempLogin();
                }
                return false;
            }
        });
        username = mUsernameET.getText().toString();

        mSigninBtn = (Button) findViewById(R.id.signingBtn);
        mSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attempLogin();
            }
        });

        mSignUpTextView = (TextView)findViewById(R.id.signUpText);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(mIntent);
            }
        });
        mSignUpEnterprise = (TextView)findViewById(R.id.signUpEnterprise);
        mSignUpEnterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(LoginActivity.this, SignUpEnterpriseActivity.class);
                startActivity(mIntent);
            }
        });
    }
    public static String getUsername()
    {
        return username;
    }

    private void attempLogin()
    {
        String Username = mUsernameET.getText().toString();
        String Password = mPasswordET.getText().toString();
        ParseUser.logInInBackground(Username, Password, new LogInCallback() {

            @Override
            public void done(ParseUser pu, ParseException e) {

                if (pu != null) {

                } else {

                }
            }
        });



        if (TextUtils.isEmpty(Username))
        {
            mUsernameET.setError(getString(R.string.error_empty_field));
            return;
        }
        else if (TextUtils.isEmpty(Password))
        {
            mPasswordET.setError(getString(R.string.error_empty_field));
            return;
        }

        SharedPreferences sharedPref = getSharedPreferences( UsernamePrefernce, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", Username);
        editor.putString("password", Password);
        editor.commit();
        new LoginUserTask(Username, Password).execute();

    }

    private class LoginUserTask extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog mProgressDialog;

        private JSONObject jsonObjectResult = null;

        private String Username;
        private String Password;

        private String error;

        private LoginUserTask(String username, String password)
        {
            Username = username;
            Password = password;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(LoginActivity.this,
                    "Processing...", "Check username and password", false, false);
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", Username));
            pairs.add(new BasicNameValuePair("password", Password));

            jsonObjectResult = jsonParser.makeHttpRequest(LOGIN_URL, pairs);
            if (jsonObjectResult == null)
            {
                error = "Error in the connection";
                return false;
            }

            try
            {
                if (jsonObjectResult.getInt("success") == 1)
                    return true;
                else
                    error = jsonObjectResult.getString("message");

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
                Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mIntent);
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "Error in the connection ", Toast.LENGTH_LONG).show();
        }
    }



}


