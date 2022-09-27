package com.wht.addekh.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

public class BottomsheetMyStoreListObject implements Parcelable
{
    private String id;
    private String user_id;
    private String store_name;
    private String category_id;
    private String store_address;
    private String store_contact_number;
    private String store_logo;
    private String store_lat;
    private String store_lng;
    private String store_desc;
    private String is_active;
    private String category_name;
    private  String store_advertisements;

    public BottomsheetMyStoreListObject(String id, String store_name) {
        this.id = id;
        this.store_name = store_name;
    }


    public BottomsheetMyStoreListObject(JSONObject jsonObject, String store_logo) {
        try {
            this.id = jsonObject.getString("id");
            this.user_id = jsonObject.getString("user_id");
            this.store_name = jsonObject.getString("store_name");
            this.category_id = jsonObject.getString("category_id");
            this.store_address = jsonObject.getString("store_address");
            this.store_contact_number = jsonObject.getString("store_contact_number");
            this.store_advertisements = jsonObject.getString("store_advertisements");
            Log.d("imageres", "BottomsheetMyStoreListObject: "+store_logo + jsonObject.getString("store_logo"));
            this.store_logo = store_logo+ jsonObject.getString("store_logo");

            this.store_desc = jsonObject.getString("store_desc");
            this.category_name = jsonObject.getString("category_name");
            this.store_lat = jsonObject.getString("store_lat");
            this.store_lng = jsonObject.getString("store_lng");

//            this.blog_image = jsonObject.getString("blog_image");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected BottomsheetMyStoreListObject(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        store_name = in.readString();
        category_id = in.readString();
        store_address = in.readString();
        store_contact_number = in.readString();
        store_logo = in.readString();
        store_lat = in.readString();
        store_lng = in.readString();
        store_desc = in.readString();
        is_active = in.readString();
        category_name = in.readString();
        store_advertisements=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(store_name);
        dest.writeString(category_id);
        dest.writeString(store_address);
        dest.writeString(store_contact_number);
        dest.writeString(store_logo);
        dest.writeString(store_lat);
        dest.writeString(store_lng);
        dest.writeString(store_desc);
        dest.writeString(is_active);
        dest.writeString(category_name);
        dest.writeString(store_advertisements);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BottomsheetMyStoreListObject> CREATOR = new Creator<BottomsheetMyStoreListObject>() {
        @Override
        public BottomsheetMyStoreListObject createFromParcel(Parcel in) {
            return new BottomsheetMyStoreListObject(in);
        }

        @Override
        public BottomsheetMyStoreListObject[] newArray(int size) {
            return new BottomsheetMyStoreListObject[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getStore_contact_number() {
        return store_contact_number;
    }

    public void setStore_contact_number(String store_contact_number) {
        this.store_contact_number = store_contact_number;
    }

    public String getStore_logo() {
        return store_logo;
    }

    public void setStore_logo(String store_logo) {
        this.store_logo = store_logo;
    }

    public String getStore_lat() {
        return store_lat;
    }

    public void setStore_lat(String store_lat) {
        this.store_lat = store_lat;
    }

    public String getStore_lng() {
        return store_lng;
    }

    public void setStore_lng(String store_lng) {
        this.store_lng = store_lng;
    }

    public String getStore_desc() {
        return store_desc;
    }

    public void setStore_desc(String store_desc) {
        this.store_desc = store_desc;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getStore_advertisements() {
        return store_advertisements;
    }

    public void setStore_advertisements(String store_advertisements) {
        this.store_advertisements = store_advertisements;
    }

    @Override
    public String toString() {
        return store_name;
    }
}
