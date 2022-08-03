package com.wht.addekho.Notification;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationInsertObject implements Parcelable {

    public String body;
    public String image;
    public String title;

    public NotificationInsertObject() {
    }

    public NotificationInsertObject(String body, String image, String title) {
        this.body = body;
        this.image = image;
        this.title = title;
    }

    protected NotificationInsertObject(Parcel in) {
        body = in.readString();
        image = in.readString();
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(body);
        dest.writeString(image);
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationInsertObject> CREATOR = new Creator<NotificationInsertObject>() {
        @Override
        public NotificationInsertObject createFromParcel(Parcel in) {
            return new NotificationInsertObject(in);
        }

        @Override
        public NotificationInsertObject[] newArray(int size) {
            return new NotificationInsertObject[size];
        }
    };

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
