package com.hassan.masla7ty.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


public class Stations implements Parcelable{

    private double area;
    private String name;
    private String specifications;

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getArea() {

        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    protected Stations(Parcel in) {
        area = in.readDouble();
        name = in.readString();
        specifications = in.readString();
    }

    public static final Creator<Stations> CREATOR = new Creator<Stations>() {
        @Override
        public Stations createFromParcel(Parcel in) {
            return new Stations(in);
        }

        @Override
        public Stations[] newArray(int size) {
            return new Stations[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(area);
        parcel.writeString(name);
        parcel.writeString(specifications);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Stations(JSONObject jsonObject){
        try{
            this.area =jsonObject.getDouble("area");
            this.name =jsonObject.optString("name");
            this.specifications =jsonObject.optString("specifications");
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
