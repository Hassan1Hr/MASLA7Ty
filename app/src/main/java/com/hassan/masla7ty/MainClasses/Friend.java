package com.hassan.masla7ty.MainClasses;

/**
 * Created by Hassan on 5/7/2015.
 */
public class Friend {
    private String friendId;
    private String firstName;
    private String lastName;
    private int  status;
    private double latitude;
    private double longitude;
    private String friendImage;


    public Friend(String friendId, String firstName, String lastName, int status, String FriendImage)
    {

        this.friendId = friendId;
        this.firstName = firstName;
        this.lastName =lastName;
        this.status = status;

        this.friendImage = FriendImage;

    }

    public Friend(String userName,String firstName, String lastName,String FriendImage)
    {
        this.friendId = userName;
        this.firstName = firstName;
        this.lastName =lastName;
       this.friendImage=FriendImage;

    }
    public Friend(String firstName, String lastName,double latitude,double longitude)
    {
        this.firstName = firstName;
        this.lastName =lastName;
        this.longitude=longitude;
        this.latitude = latitude;

    }

    public  String getFirstName()
    {
        return firstName;
    }
    public void setFirstName(String firstName)
    {
        this.firstName=firstName;
    }


    public  String getFriendId()
    {
        return friendId;
    }
    public void setFriendId(String friendId)
    {
        this.friendId=friendId;
    }
    public  String getLastName()
    {
        return lastName;
    }
    public void setLastName(String lastName)
    {
        this.lastName=lastName;
    }
    public  int getStatus()
    {
        return status;
    }
    public void setStatus(int status)
    {
        this.status=status;
    }
    public  double getLongitude()
    {
        return longitude;
    }
    public void setLatitude(double latitude)
    {
        this.latitude=latitude;
    }
    public  double getLatitude()
    {
        return latitude;
    }
    public void setLongitude(double longitude)
    {
        this.longitude=longitude;
    }
    public  String getFriendImage()
    {
        return friendImage;
    }
    public void setFriendImage(String FriendImage)
    {
        this.friendImage=FriendImage;
    }
}
