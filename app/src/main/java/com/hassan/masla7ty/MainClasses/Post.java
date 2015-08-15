package com.hassan.masla7ty.MainClasses;

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



    private int numOfLikes = 0;
    private String likes;


    public Post(int postId , String firstName, String lastName, String description, String mNewsDate, String mTime, String photo,String  userImage,int numOflikes, String like){//,String userImage) {
        this.postId = postId;
        this.userPhoto = userImage;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mNewsBody = description;
        this.mNewsDate = mNewsDate;
         this.likes = like;
        this.mTime =mTime;
        this.photo = photo;
        this.numOfLikes = numOflikes;
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

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getLikes() {
        return likes;
    }
    public int getNumOfLikes() {
        return numOfLikes;
    }
    public void setNumOfLikes(int numOfLikes) {
        this.numOfLikes = numOfLikes;
    }
}
