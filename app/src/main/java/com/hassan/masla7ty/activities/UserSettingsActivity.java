package com.hassan.masla7ty.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hassan.masla7ty.mainclasses.JSONParser;
import com.hassan.masla7ty.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserSettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String CHANGE_SETTINGS_URL = "http://masla7tyfinal.esy.es/app/setting.php";
    private JSONParser jsonParser = new JSONParser();

    private String oldUserName;
    private String oldPassword;

    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String gender;
    private String age;
    private String city;
    private String mobile;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_settings);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        username = firstname = lastname = password = mobile = "";
        if (key.equals("email")) {
            Preference pref = findPreference(key);
            pref.setSummary(sharedPreferences.getString(key, ""));
            username = sharedPreferences.getString(key, "");
            Toast.makeText(getApplicationContext(), R.string.email_changed, Toast.LENGTH_LONG).show();

        }
        if (key.equals("firstname")) {
            Preference pref = findPreference(key);
            pref.setSummary(sharedPreferences.getString(key, ""));
            firstname = sharedPreferences.getString(key, "");

        }

        if (key.equals("lastname")) {
            Preference pref = findPreference(key);
            pref.setSummary(sharedPreferences.getString(key, ""));
            lastname = sharedPreferences.getString(key, "");
        }

        if (key.equals("password")) {
            Preference pref = findPreference(key);
            pref.setSummary(sharedPreferences.getString(key, ""));
            password = sharedPreferences.getString(key, "");
        }
        if (key.equals("phone")) {
            Preference pref = findPreference(key);
            pref.setSummary(sharedPreferences.getString(key, ""));
            mobile = sharedPreferences.getString(key, "");
        }
        showLoginDialog();
    }

    private void showLoginDialog()
    {
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.userpasslayout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);
        final EditText user = (EditText) prompt.findViewById(R.id.login_name);
        final EditText pass = (EditText) prompt.findViewById(R.id.login_password);
        //user.setText(Login_USER); //login_USER and PASS are loaded from previous session (optional)
        //pass.setText(Login_PASS);
        alertDialogBuilder.setTitle(R.string.login);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        oldPassword = pass.getText().toString();
                        oldUserName = user.getText().toString();
                        try {
                            {
                                new ChangeSettingsTask(oldUserName, oldPassword, username, firstname, lastname, password, mobile).execute();
                            }
                        } catch (Exception e) {
                            Toast.makeText(UserSettingsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();

            }
        });

        alertDialogBuilder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceManager()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceManager()
                .getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private class ChangeSettingsTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject jsonObjectResult = null;

        private String error;
        String userName;


        private ChangeSettingsTask(String firstname, String lastname,String username,String userpassword,String usergender,String userage,String usercity,String usermobile)
        {
            UserSettingsActivity.this.firstname=firstname;
            UserSettingsActivity.this.lastname=lastname;
            UserSettingsActivity.this.username=username;
            password =userpassword;
            gender =usergender;
            age =userage;
            city = usercity;
            mobile = usermobile;

        }

        private ChangeSettingsTask(String oldUserName, String oldPassword, String firstname, String lastname, String username, String userpassword, String usermobile) {

            UserSettingsActivity.this.oldUserName=oldUserName;
            UserSettingsActivity.this.oldPassword=oldPassword;

            UserSettingsActivity.this.firstname=firstname;
            UserSettingsActivity.this.lastname=lastname;
            UserSettingsActivity.this.username=username;
            password =userpassword;
            mobile = usermobile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", oldUserName));
            pairs.add(new BasicNameValuePair("password", oldPassword));
            pairs.add(new BasicNameValuePair("newFirstName", firstname));
            pairs.add(new BasicNameValuePair("newLastName", lastname));
            pairs.add(new BasicNameValuePair("newUserName", username));
            pairs.add(new BasicNameValuePair("newPassword", password));
            //pairs.add(new BasicNameValuePair("gender", gender));
            //pairs.add(new BasicNameValuePair("age", age));
            //pairs.add(new BasicNameValuePair("city", city));
            pairs.add(new BasicNameValuePair("newMobile", mobile));
            jsonObjectResult = jsonParser.makeHttpRequest(CHANGE_SETTINGS_URL, pairs);
            if (jsonObjectResult == null)
            {
                error =  getBaseContext().getResources().getString(R.string.error);
                return false;
            }

            try {
                if (jsonObjectResult.getInt("success") == 1)
                    return true;
                else
                    error = jsonObjectResult.getString("message");

            } catch (Exception ex) {

            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Toast.makeText(getApplicationContext(), R.string.your_setting_changed, Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(getApplicationContext(), R.string.your_setting_not_changed, Toast.LENGTH_LONG).show();
        }
    }
}