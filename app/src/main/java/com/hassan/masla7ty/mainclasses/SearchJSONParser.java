package com.hassan.masla7ty.mainclasses;

import android.annotation.TargetApi;
import android.database.MatrixCursor;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sherif on 29/06/15.
 */
public class SearchJSONParser {

    private String firstName;
    private String userName;
    private String lastName;
    private String name;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public MatrixCursor parse(JSONObject jObject) {

        JSONArray jUsers = null;
        MatrixCursor mc = null;
        try {
            /** Retrieves all the elements in the 'places' array */

            jUsers = jObject.getJSONArray("users");
            mc = new MatrixCursor(new String[]{"_id", "firstName", "userName"});
            for (int i = 0; i < jUsers.length(); i++) {
                JSONObject jo = jUsers.getJSONObject(i);
                // extract the properties from the JSONObject and use it with the addRow() method below
                userName = jo.getString("username");
                firstName = jo.getString("firstName");
                lastName = jo.getString("lastName");
                name = firstName+ " " +lastName;
                mc.newRow()
                        .add(i)
                        .add("firstName", name)
                        .add("userName", userName);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mc;
    }
}