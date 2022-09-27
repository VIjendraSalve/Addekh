package com.wht.addekh.Model;

import org.json.JSONObject;

public class Template {

    private String id;
    private String image;
    private String sequence;

    public Template() {
    }

    public Template(JSONObject jsonObject, String store_logo) {
        try {
            this.id = jsonObject.getString("id");
            this.image = store_logo + jsonObject.getString("image");
            this.sequence = jsonObject.getString("sequence");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
