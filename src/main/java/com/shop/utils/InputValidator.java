package com.shop.utils;

/**
 * Utility class that provides input validation methods.
 * Used across the system to validate user inputs
 * before processing them.
 * All methods return true if input is valid
 * and false if input is invalid.
 */
public class InputValidator {

    /**
     * Checks if the given input string is not null and not empty.
     */
    public boolean isValidInput(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Checks if the given quantity is valid.
     * Quantity must be greater than zero.
     */
    public boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }

    /**
     * Checks if the given product ID is valid.
     * Valid format is P001, P002 etc.
     */
    public boolean isValidProductId(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            return false;
        }
        return productId.trim().matches("P[0-9]{3}");
    }

    /**
     * Checks if the given menu choice is valid.
     * Choice must be between min and max.
     */
    public boolean isValidMenuChoice(int choice, int min, int max) {
        return choice >= min && choice <= max;
    }

    /**
     * Checks if the given price is valid.
     * Price must be greater than zero.
     */
    public boolean isValidPrice(double price) {
        return price > 0;
    }

    /**
     * Checks if the given amount is valid.
     * Amount must be greater than zero.
     */
    public boolean isValidAmount(double amount) {
        return amount > 0;
    }
}