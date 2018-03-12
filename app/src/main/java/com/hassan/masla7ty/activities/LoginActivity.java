
package com.hassan.masla7ty.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hassan.masla7ty.MainClasses.JSONParser;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.pojo.ApplicationURL;
import com.hassan.masla7ty.pojo.MyApplication;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameET;
    private EditText mPasswordET;
    protected TextView mSignUpTextView;
    protected TextView mSignUpEnterprise;
    private Button mSigninBtn;
    private static String username;
    private static final String TAG = "Login Activity";
    private FirebaseAuth mAuth;
    private JSONParser jsonParser = new JSONParser();

    private String LOGIN_URL = ApplicationURL.appDomain.concat("signin.php");


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        SharedPreferences sharedPref =getSharedPreferences(MyApplication.UsernamePrefernce, Context.MODE_PRIVATE);
        String userNamePreference= sharedPref.getString("username", "notfound");
        String passwordPreference= sharedPref.getString("password", "notfound");
        if(userNamePreference !="notfound" && passwordPreference != "notfound"&&currentUser != null)
        {
            Go();

        }
    }

    public static String getUsername()
    {
        return username;
    }

    private void attempLogin()
    {
        String Username = mUsernameET.getText().toString();
        String Password = mPasswordET.getText().toString();

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
            if (aBoolean)
            {
                mAuth.signInWithEmailAndPassword(Username, Password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    SharedPreferences sharedPref = getSharedPreferences( MyApplication.UsernamePrefernce, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("username", Username);
                                    editor.putString("password", Password);
                                    editor.commit();
                                    Go();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                // [END_EXCLUDE]
                            }
                        });
                mProgressDialog.dismiss();
            }
            else {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error in the connection ", Toast.LENGTH_LONG).show();
            }
            }
    }


    private void Go() {
        Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mIntent);
        finish();
    }


}


