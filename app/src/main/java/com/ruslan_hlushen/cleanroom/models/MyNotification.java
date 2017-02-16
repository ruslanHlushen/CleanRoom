package com.ruslan_hlushen.cleanroom.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ruslan on 29.01.2017.
 */

public class MyNotification implements Parcelable {

    public static final int newNotificationType = 0;
    public static final int answerNotificationType = 1;
    public static final int infoNotificationType = 2;
    public static final Creator<MyNotification> CREATOR = new Creator<MyNotification>() {
        @Override
        public MyNotification createFromParcel(Parcel source) {return new MyNotification(source);}


        @Override
        public MyNotification[] newArray(int size) {return new MyNotification[size];}
    };
    Integer Floor;
    private int NotificationType;
    private int Id;
    private String Message;
    private Long TimeSend;
    private String UserLogin;


    public MyNotification() {}


    protected MyNotification(Parcel in) {

        this.NotificationType = in.readInt();
        this.Id = in.readInt();
        this.Message = in.readString();
        this.TimeSend = (Long) in.readValue(Long.class.getClassLoader());
        this.UserLogin = in.readString();
        this.Floor = (Integer) in.readValue(Integer.class.getClassLoader());
    }


    public int getNotificationType() { return NotificationType; }


    public int getId() { return Id; }


    public String getMessage() { return (Message != null ? Message : ""); }


    public long getTimeSend() { return TimeSend; }


    public Integer getFloor() { return Floor; }


    public String getUserLogin() { return (UserLogin != null ? UserLogin : "???"); }


    public String getStringDateWithTime(String timeUnknown) {

        if (TimeSend != null) {

            DateFormat df = new SimpleDateFormat("HH:mm / dd.MM.yyyy");
            Date date = new Date(TimeSend);
            return String.valueOf(df.format(date));

        } else {
            return timeUnknown;
        }
    }


    public String getStringDate(String timeUnknown) {

        if (TimeSend != null) {

            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            Date date = new Date(TimeSend);
            return String.valueOf(df.format(date));

        } else {
            return timeUnknown;
        }
    }


    public String getMessageWithFloor(String floorText) {

        String result = "";
        result += (getFloor() != null ? String.valueOf(getFloor()) : "??");
        result += (" " + floorText + " " + getMessage());
        return result;
    }


    @Override
    public int describeContents() { return 0; }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.NotificationType);
        dest.writeInt(this.Id);
        dest.writeString(this.Message);
        dest.writeValue(this.TimeSend);
        dest.writeString(this.UserLogin);
        dest.writeValue(this.Floor);
    }
}