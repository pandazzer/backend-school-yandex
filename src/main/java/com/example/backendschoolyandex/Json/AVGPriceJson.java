package com.example.backendschoolyandex.Json;


public class AVGPriceJson {
    private String type;
    private String name;
    private String id;
    private int price;
    private String parentId;
    private String date;
    AVGPriceJson[] children;

    public AVGPriceJson(String type, String name, String id, int price, String parentId, String date, AVGPriceJson[] children) {
        this.type = type;
        this.name = name;
        this.id = id;
        this.price = price;
        this.parentId = parentId;
        this.date = date;
        this.children = children;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AVGPriceJson[] getChildren() {
        return children;
    }

    public void setChildren(AVGPriceJson[] children) {
        this.children = children;
    }
}

