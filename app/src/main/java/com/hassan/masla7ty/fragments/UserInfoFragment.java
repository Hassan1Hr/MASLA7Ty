package com.hassan.masla7ty.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hassan.masla7ty.R;
import com.hassan.masla7ty.activities.LoginActivity;
import com.hassan.masla7ty.MainClasses.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hassan on 6/25/2015.
 */
public class UserInfoFragment extends Fragment {


    private UserInfo userInfo;

    private Context mcontext;
    private TextView name;

    private TextView gender;
    private TextView age;
    private TextView city;
    private TextView number;



    private JSONParser jsonParser = new JSONParser();

    private String READNEWS_URL =
            "http://masla7ty.esy.es/app/getfriend_controller.php";
    public static final String ARG_PAGE = "ARG_PAGE";



    public static UserInfoFragment newInstance() {

        UserInfoFragment fragment = new UserInfoFragment();

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_info, container, false);
        name = (TextView)view.findViewById(R.id.nameText);
        gender = (TextView)view.findViewById(R.id.genderText);
        age = (TextView)view.findViewById(R.id.ageText);
        city = (TextView)view.findViewById(R.id.cityText);
        number = (TextView)view.findViewById(R.id.mobileText);


        // Bind adapter to recycler
        new GetUserUnfoTask().execute();
        return  view;
    }
    public  class GetUserUnfoTask extends AsyncTask<Void, Void, Boolean>
    {


        private JSONObject jsonObjectResult = null;

        private String error;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();



        }



        @Override
        protected Boolean doInBackground(Void... params)
        {

            /*
            Location mloLocation = FriendFragment.newInstance().getCurrentLocation();
            if(mloLocation!=null) {
               latitude = (double) (mloLocation.getLatitude());
                longitude = (double) mloLocation.getLongitude();
            }*/
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();

            pairs.add(new BasicNameValuePair("userName", LoginActivity.getUsername()));

            jsonObjectResult = jsonParser.makeHttpRequest(READNEWS_URL, pairs);

            if (jsonObjectResult == null)
            {
                error = "Error in the connection";
                return false;
            }

            try
            {
                if (jsonObjectResult.getInt("success") == 1)
                {
                    JSONArray jsonArray = jsonObjectResult.getJSONArray("Get_friends");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject friends = jsonArray.getJSONObject(i);

                         userInfo = new UserInfo
                                (
                                        friends.getString("firstName"),
                                        friends.getString("lastName"),
                                        friends.getString("gender"),
                                        friends.getInt("age"),
                                        friends.getString("city"),
                                        friends.getInt("mobile")


                                );

                    }
                    return true;
                }
                else
                    error = "No Info";

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
                name.setText(userInfo.getName()+" "+userInfo.getLastName());
                gender.setText(userInfo.getGender());
                age.setText(userInfo.getAge());
                city.setText(userInfo.getCity());
                number.setText(userInfo.getNumber());

            }


            else {



                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        }
    }
    public class UserInfo{
        String firstName;
        String lastName;
        String gender;
        int age;
        String city;

        int number;

        public UserInfo(String firstName, String lastName, String gender, int age, String city, int mobile) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
            this.age = age;
            this.city = city;
            this.number = number;
        }

        public String getName() {
            return firstName;
        }
        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public int getAge() {
            return age;
        }

        public String getCity() {
            return city;
        }

        public String getGender() {
            return gender;
        }

        public int getNumber() {
            return number;
        }

        public void setName(String name) {
            this.firstName = name;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
