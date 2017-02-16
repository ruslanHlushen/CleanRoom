package com.ruslan_hlushen.cleanroom.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Руслан on 01.12.2016.
 */
public class Hostel implements Parcelable {

    public static final Parcelable.Creator<Hostel> CREATOR = new Parcelable.Creator<Hostel>() {
        @Override
        public Hostel createFromParcel(Parcel source) {return new Hostel(source);}


        @Override
        public Hostel[] newArray(int size) {return new Hostel[size];}
    };
    public String HostelName;
    private Integer HostelVkId;


    public Hostel() {}


    protected Hostel(Parcel in) {

        this.HostelName = in.readString();
        this.HostelVkId = (Integer) in.readValue(Integer.class.getClassLoader());
    }


    public Integer getHostelVkId() { return HostelVkId; }


    public String getHostelName() { return HostelName; }


    @Override
    public int describeContents() { return 0; }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.HostelName);
        dest.writeValue(this.HostelVkId);
    }
}
