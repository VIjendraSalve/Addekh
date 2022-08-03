package com.wht.addekho.Model;

import java.io.Serializable;

public class CategorySearchObject implements Serializable {


    private static final long serialVersionUID = 0L;
    public String id;
    public String name;
    public String image;
    public String sequence;
    public String is_active;


    public CategorySearchObject() {
    }


    public CategorySearchObject(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

}
