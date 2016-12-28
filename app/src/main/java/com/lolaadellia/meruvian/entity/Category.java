package com.lolaadellia.meruvian.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hanum on 27/12/2016.
 */

public class Category {

    private int id = -1;
    private String category;
    private String subcategory;
    private long createDate;
    private int status;

    public static List<Category> data() {
        List<Category> categories = new ArrayList<>();
        Category category = new Category();
        category.setId(1);
        category.setCategory("");
        category.setSubcategory("");
        category.setCreateDate(new Date().getTime());
        categories.add(category);

        Category category2 = new Category();
        category2.setId(2);
        category2.setCategory("");
        category2.setSubcategory("");
        category2.setCreateDate(new Date().getTime());
        categories.add(category2);
        return categories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
