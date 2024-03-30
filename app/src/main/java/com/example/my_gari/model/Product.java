package com.example.my_gari.model;

import com.hishd.tinycart.model.Item;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable{
    int totalPrice;
    String description;
    String discount;
    String image;
    String name;
    int price;
    int quantity;
    String status;
    int stock;
    public Product(){

    }
    public Product(String description,String discount, String image, String name, int price, int quantity, String status, int stock,int totalPrice){
        this.description = description;
        this.discount = discount;
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.stock = stock;
        this.totalPrice = totalPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

}
