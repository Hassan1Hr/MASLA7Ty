package com.hassan.masla7ty.MainClasses;

import android.graphics.Bitmap;

/**
 * Created by Bassam on 10/21/2014.
 */
public class Post {

    private String    mTime;
    private String mNewsTitle;
    private String mNewsDate;
    private String mNewsBody;
    private String firstName;
    private String lastName;
    private int postId;
    private String photo;
    private String userPhoto;
    private Bitmap bitmap;
    private Bitmap userBitmap;

    public Post(int postid, String firstName, String lastName, String description, String mNewsDate, String mTime, String photo){//,String userImage) {
        this.postId = postid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mNewsBody = description;
        this.mNewsDate = mNewsDate;
       // this.userPhoto = userImage;
        this.mTime =mTime;
        this.photo = photo;
    }
    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
    }
    public String getmNewsFirstName() {
        return firstName;
    }

    public void setmNewsFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getmNewsLastName() {
        return lastName;
    }

    public void setmNewsLastName(String firstName) {
        this.lastName = lastName;
    }

    public String getmNewsTime() {
        return mTime;
    }

    public void setmNewsTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmNewsDate() {
        return mNewsDate;
    }

    public void setmNewsDate(String mNewsDate) {
        this.mNewsDate = mNewsDate;
    }

    public String getmNewsBody() {
        return mNewsBody;
    }

    public void setmNewsBody(String mNewsBody) {
        this.mNewsBody = mNewsBody;
    }
    public String getUserPhoto() {
        return userPhoto;
    }
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
    public Bitmap getUserBitmap() {
        return userBitmap;
    }
    public void setUserBitmap(Bitmap userBitmap) {
        this.userBitmap = userBitmap;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
