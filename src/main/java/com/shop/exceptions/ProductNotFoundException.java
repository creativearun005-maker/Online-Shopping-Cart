package com.shop.exceptions;

/**
 * Thrown when a requested product is not found in the system.
 */
public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String message) {
        super(message);
    }
}