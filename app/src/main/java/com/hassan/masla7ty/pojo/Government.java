package com.hassan.masla7ty.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Government implements Parcelable {

    int id;
    private String name;
    private ArrayList<Stations> stations;

    public Government(){}

    public String getName() {
        return name;
    }


    public ArrayList<Stations> getStations() {
        return stations;
    }

    protected Government(Parcel in) {
        id = in.readInt();
        name = in.readString();
        stations = in.createTypedArrayList(Stations.CREATOR);
    }

    public static final Creator<Government> CREATOR = new Creator<Government>() {
        @Override
        public Government createFromParcel(Parcel in) {
            return new Government(in);
        }

        @Override
        public Government[] newArray(int size) {
            return new Government[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedList(stations);
    }


    public Government(JSONObject jsonObject){
        try{
            this.name=jsonObject.getString("name");
            this.stations =new ArrayList<>();
            JSONArray ingredientsJsonArray=jsonObject.getJSONArray("ingredients");
            for(int i=0;i < ingredientsJsonArray.length();i++){
                stations.add(new Stations(ingredientsJsonArray.getJSONObject(i)));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
