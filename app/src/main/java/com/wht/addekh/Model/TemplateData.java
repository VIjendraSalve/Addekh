package com.wht.addekh.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TemplateData implements Parcelable {

    private String id;
    private String template_name;
    private String image;
    private String name;
    private ArrayList<TemplateFields> templateFieldsArrayList = new ArrayList<>();

    public TemplateData(JSONObject jsonObject, String store_logo) {
        try {
            this.id = jsonObject.getString("id");
            this.template_name =  jsonObject.getString("template_name");
            //this.template_code = jsonObject.getString("template_code");
            //this.tempate_category_id = jsonObject.getString("tempate_category_id");
            this.image = store_logo + jsonObject.getString("image");
            //this.template_category_id = jsonObject.getString("template_category_id");
            this.name = jsonObject.getString("name");

            JSONArray jsonArray = jsonObject.getJSONArray("offer_fields");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Log.d("Valuess", "TemplateData: "+jsonObject1.getString("field_name"));
                templateFieldsArrayList.add(new TemplateFields(jsonObject1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected TemplateData(Parcel in) {
        id = in.readString();
        template_name = in.readString();
        image = in.readString();
        name = in.readString();
        templateFieldsArrayList = in.createTypedArrayList(TemplateFields.CREATOR);
    }

    public static final Creator<TemplateData> CREATOR = new Creator<TemplateData>() {
        @Override
        public TemplateData createFromParcel(Parcel in) {
            return new TemplateData(in);
        }

        @Override
        public TemplateData[] newArray(int size) {
            return new TemplateData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplate_name() {
        return template_name;
    }

    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<TemplateFields> getTemplateFieldsArrayList() {
        return templateFieldsArrayList;
    }

    public void setTemplateFieldsArrayList(ArrayList<TemplateFields> templateFieldsArrayList) {
        this.templateFieldsArrayList = templateFieldsArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(template_name);
        parcel.writeString(image);
        parcel.writeString(name);
        parcel.writeTypedList(templateFieldsArrayList);
    }
}
