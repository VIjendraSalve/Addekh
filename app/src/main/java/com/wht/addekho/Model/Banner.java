package com.wht.addekho.Model;

import org.json.JSONObject;

public class Banner {

    private String id;
    private String offer_name;
    private String template_link;

    public Banner() {
    }

    public Banner(JSONObject jsonObject, String store_logo) {
        try {
            this.id = jsonObject.getString("id");
            this.offer_name = store_logo+jsonObject.getString("offer_name");
            this.template_link = jsonObject.getString("template_link");

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

    public String getimage() {
        return offer_name;
    }

    public void setimage(String image) {
        this.offer_name = image;
    }

    public String gettemplate_link() {
        return template_link;
    }

    public void settemplate_link(String template_link) {
        this.template_link = template_link;
    }

}
