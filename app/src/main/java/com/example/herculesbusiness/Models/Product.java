package com.example.herculesbusiness.Models;

import java.io.Serializable;

public class Product implements Serializable {
    private String ProductID;
    private String ProductName;
    private int Quantity, multiplier;



    private int Price;
    private Integer ImageResource;

    public Product() {
    }

    public Product(String productID, Integer imageResource, String productName, int quantity, int price, int multiplier) {
        ProductID = productID;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        ImageResource = imageResource;
        this.multiplier = multiplier;
    }

    public String getProductID() {
        return ProductID;
    }
    public Integer getImageResource() {
        return ImageResource;
    }
    public void setProductID(String productID) {
        this.ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        this.ProductName = productName;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getPrice() {
        return Price;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public void setImageResource(Integer imageResource) {
        ImageResource = imageResource;
    }

    public void setPrice(int price) {
        Price = price;
    }
}
