package com.shop.exceptions;

/**
 * Thrown when a requested product is out of stock.
 */
public class OutOfStockException extends Exception {

    public OutOfStockException(String message) {
        super(message);
    }
}