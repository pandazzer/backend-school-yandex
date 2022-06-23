package com.example.backendschoolyandex.Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(nullable = false)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(name = "parentid")
    private String parentId;
    @Column()
    private Integer price;
    @Column(nullable = false)
    private String type;
    @Column(name = "updatedate", columnDefinition = "TIMESTAMP")
    private Date updateDate;


    public Product() {
    }

    public Product(String id, String name, String parentId, Integer price, String type, Date updateDate) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.price = price;
        this.type = type;
        this.updateDate = updateDate;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
