package com.hassan.masla7ty.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hassan.masla7ty.mainclasses.JSONParser;
import com.hassan.masla7ty.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignUpEnterpriseActivity extends AppCompatActivity {
    protected EditText mUsername;
    protected EditText mPassword;
    protected EditText mEmail;
    protected EditText mDescription;
    protected Button mSignUpButton;
    protected Button mCancelButton;
    private JSONParser jsonParser = new JSONParser();
    private String REGISTER_URL =
            "http://10.0.0.35:/newsite/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_enterprise);


        mUsername = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mEmail = (EditText) findViewById(R.id.emailField);
        mDescription = (EditText) findViewById(R.id.description);

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
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();
                String Description = mDescription.getText().toString();

                username = username.trim();
                password = password.trim();
                email = email.trim();
                Description = Description.trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty() || Description.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpEnterpriseActivity.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // create the new user!
                    new RegisterUserTask(username, password).execute();


                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_enterprise, menu);
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

            private String Username;
            private String Password;

            private String error;

            private RegisterUserTask(String username, String password)
            {
                Username = username;
                Password = password;
            }

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                mProgressDialog = ProgressDialog.show(SignUpEnterpriseActivity.this,
                        getString(R.string.processing), getString(R.string.create_new_user), false, false);
            }

            @Override
            protected Boolean doInBackground(Void... params)
            {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("username", Username));
                pairs.add(new BasicNameValuePair("password", Password));

                jsonObjectResult = jsonParser.makeHttpRequest(REGISTER_URL, pairs);
                if (jsonObjectResult == null)
                {
                    error =  getBaseContext().getResources().getString(R.string.error);
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
                    Intent mIntent = new Intent(SignUpEnterpriseActivity.this, LoginActivity.class);
                    startActivity(mIntent);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        }
}
