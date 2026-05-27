package com.shop.models;

/**
 * Represents a product available in the shop.
 * Stores all details related to a product.
 */
public class Product {

    private String productId;
    private String productName;
    private double price;
    private int stockQuantity;
    private String category;

    // Constructor
    public Product(String productId, String productName,
                   double price, int stockQuantity,
                   String category) {
        this.productId     = productId;
        this.productName   = productName;
        this.price         = price;
        this.stockQuantity = stockQuantity;
        this.category      = category;
    }

    // Getters
    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getCategory() {
        return category;
    }

    // Setters
    public void setPrice(double price) {
        this.price = price;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    // Display product details
    public String toString() {
        return "Product ID   : " + productId +
                "\nName         : " + productName +
                "\nCategory     : " + category +
                "\nPrice        : Rs." + price +
                "\nStock        : " + stockQuantity + " items";
    }
}