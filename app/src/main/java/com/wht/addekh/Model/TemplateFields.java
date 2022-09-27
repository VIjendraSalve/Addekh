package com.wht.addekh.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

public class TemplateFields implements Parcelable {

    private String id;
    private String template_id;
    private String field_name;

    public TemplateFields(JSONObject jsonObject) {
        try {

            Log.d("Testtt", "TemplateFields: "+jsonObject.getString("id"));
            Log.d("Testtt", "TemplateFields: "+jsonObject.getString("template_id"));
            Log.d("Testtt", "TemplateFields: "+jsonObject.getString("field_name"));
            this.id = jsonObject.getString("id");
            this.template_id = jsonObject.getString("template_id");
            this.field_name = jsonObject.getString("field_name");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected TemplateFields(Parcel in) {
        id = in.readString();
        template_id = in.readString();
        field_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(template_id);
        dest.writeString(field_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TemplateFields> CREATOR = new Creator<TemplateFields>() {
        @Override
        public TemplateFields createFromParcel(Parcel in) {
            return new TemplateFields(in);
        }

        @Override
        public TemplateFields[] newArray(int size) {
            return new TemplateFields[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }
}
