package com.wht.addekho.Model;

import org.json.JSONObject;

public class Advertisment {

    private String id;
    private String store_id;
    private String category_id;
    private String banner_image;
    private String title;
    private String description;
    private String tags;
    private String last_date;
    private String advt_lat;
    private String advt_lng;
    private String store_name;
    private String store_address;
    private String store_logo;
    private String category_name;
    private String created_on;
    private String is_active;
    private String is_like;

    public Advertisment() {
    }

    public Advertisment(JSONObject jsonObject, String store_logo, String banner_image) {
        try {
            this.id = jsonObject.getString("id");
            this.store_id = jsonObject.getString("store_id");
            this.category_id = jsonObject.getString("category_id");
            this.banner_image = banner_image + jsonObject.getString("banner_image");
            this.title = jsonObject.getString("title");
            this.description = jsonObject.getString("description");
            this.tags = jsonObject.getString("tags");
            this.last_date = jsonObject.getString("last_date");
            this.advt_lat = jsonObject.getString("advt_lat");
            this.advt_lng = jsonObject.getString("advt_lng");
            this.store_name = jsonObject.getString("store_name");
            this.store_address = jsonObject.getString("store_address");
            this.store_logo = store_logo + jsonObject.getString("store_logo");
            this.category_name = jsonObject.getString("category_name");
            this.created_on = jsonObject.getString("created_on");
            this.is_active = jsonObject.getString("is_active");
            this.is_like = jsonObject.getString("is_like");

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

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLast_date() {
        return last_date;
    }

    public void setLast_date(String last_date) {
        this.last_date = last_date;
    }

    public String getAdvt_lat() {
        return advt_lat;
    }

    public void setAdvt_lat(String advt_lat) {
        this.advt_lat = advt_lat;
    }

    public String getAdvt_lng() {
        return advt_lng;
    }

    public void setAdvt_lng(String advt_lng) {
        this.advt_lng = advt_lng;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getStore_logo() {
        return store_logo;
    }

    public void setStore_logo(String store_logo) {
        this.store_logo = store_logo;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }
}
