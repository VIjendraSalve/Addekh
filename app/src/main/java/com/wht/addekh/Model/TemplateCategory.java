package com.wht.addekh.Model;

import org.json.JSONObject;

public class TemplateCategory {
    private String template_category_id;
    private String name;
    private String image;
    private String is_active;

    public TemplateCategory(String template_category_id, String name, String image, String is_active) {
        this.template_category_id = template_category_id;
        this.name = name;
        this.image = image;
        this.is_active = is_active;
    }

    public TemplateCategory(String name, String image, String is_active) {
        this.name = name;
        this.image = image;
        this.is_active = is_active;
    }

    public TemplateCategory() {
    }

    public TemplateCategory(TemplateCategory categoryObject) {
        this.template_category_id = categoryObject.template_category_id;
        this.name = categoryObject.name;
        this.image = categoryObject.image;
        this.is_active = categoryObject.is_active;

    }

    public TemplateCategory(JSONObject jsonObject, String image) {
        try {

            this.template_category_id = jsonObject.getString("template_category_id");
            this.name = jsonObject.getString("name");
            this.is_active = jsonObject.getString("is_active");
            this.image = image + jsonObject.getString("image");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return template_category_id;
    }

    public void setId(String id) {
        this.template_category_id = template_category_id;
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

