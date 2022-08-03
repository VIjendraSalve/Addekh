package com.wht.addekho.Model;

import org.json.JSONObject;

public class AllCategory {
    private String id;
    private String name;
    private String image;
    private String is_active;

    public AllCategory(String id, String name, String image, String is_active) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.is_active = is_active;
    }

    public AllCategory(String name, String image, String is_active) {
        this.name = name;
        this.image = image;
        this.is_active = is_active;
    }

    public AllCategory() {
    }

    public AllCategory(AllCategory categoryObject) {
        this.id = categoryObject.id;
        this.name = categoryObject.name;
        this.image = categoryObject.image;
        this.is_active = categoryObject.is_active;

    }

    public AllCategory(JSONObject jsonObject, String image) {
        try {

            this.id = jsonObject.getString("id");
            this.name = jsonObject.getString("name");
            this.is_active = jsonObject.getString("is_active");
            this.image = image + jsonObject.getString("image");


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

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }


    @Override
    public String toString() {
        return name;
    }
}

