package com.hassan.masla7ty.MainClasses;
public class FriendComment extends Friend{


    String comment;
    String date;


    String time;



    public FriendComment( String userName,String firstName, String lastName, String FriendImage,String comment,String date,String time) {
        super(userName,firstName, lastName, FriendImage);
        this.comment = comment;
        this.date = date;
        this.time = time;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}