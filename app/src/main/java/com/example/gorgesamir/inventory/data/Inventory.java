package com.example.gorgesamir.inventory.data;

import android.graphics.Bitmap;

/**
 * Created by gorge samir on 2018-02-13.
 */

public class Inventory {

    private String productName;
    private String productDescription;
    private int productQuantity;
    private int productPrice;
    private Bitmap productImage;
//    private Bitmap emptyBitMap;

    public Inventory() {
    }

    public Inventory(String productName, String productDescription, int productQuantity, int productPrice, Bitmap productImage) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.productImage = productImage;
    }

    public Inventory(String productName, String productDescription, int productQuantity, int productPrice) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public Bitmap getProductImage() {
        return productImage;
    }

    public void setProductImage(Bitmap productImage) {
        this.productImage = productImage;
    }

}
