package com.models;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private long code;
    @NotNull
    private String name;
    @Min(0)
    private float pric;

    @ManyToOne()
    private Category category;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPric() {
        return pric;
    }

    public void setPric(float pric) {
        this.pric = pric;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
