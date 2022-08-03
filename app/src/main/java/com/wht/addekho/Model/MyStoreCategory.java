package com.wht.addekho.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class MyStoreCategory implements Parcelable
{
    private String id;
    private String name;


    public MyStoreCategory(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString("name");
            this.id = jsonObject.getString("id");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected MyStoreCategory(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<MyStoreCategory> CREATOR = new Creator<MyStoreCategory>() {
        @Override
        public MyStoreCategory createFromParcel(Parcel in) {
            return new MyStoreCategory(in);
        }

        @Override
        public MyStoreCategory[] newArray(int size) {
            return new MyStoreCategory[size];
        }
    };

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
    }

    @Override
    public String toString() {
        return  name;
    }
}

