package com.wht.addekh.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class GenderObject implements Parcelable {

    public String id;
    public String strGender;

    public GenderObject() {
    }

    public GenderObject(String id, String strGender) {
        this.id = id;
        this.strGender = strGender;
    }

    protected GenderObject(Parcel in) {
        id = in.readString();
        strGender = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(strGender);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GenderObject> CREATOR = new Creator<GenderObject>() {
        @Override
        public GenderObject createFromParcel(Parcel in) {
            return new GenderObject(in);
        }

        @Override
        public GenderObject[] newArray(int size) {
            return new GenderObject[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStrGender() {
        return strGender;
    }

    public void setStrGender(String strGender) {
        this.strGender = strGender;
    }

    @Override
    public String toString() {
        return strGender;
    }
}

