package com.wht.addekho.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class ProductListObject implements Parcelable {


    public String id;

    public String name;
    public String image;


    public ProductListObject() {
    }

    public ProductListObject(String id,String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public ProductListObject(JSONObject object, String items_image_path) {
        try {
            this.id = object.getString("id");
            this.name = object.getString("name");
            this.image = items_image_path + object.getString("image");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected ProductListObject(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
    }

    public static final Creator<ProductListObject> CREATOR = new Creator<ProductListObject>() {
        @Override
        public ProductListObject createFromParcel(Parcel in) {
            return new ProductListObject(in);
        }

        @Override
        public ProductListObject[] newArray(int size) {
            return new ProductListObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(image);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

