package com.shop.interfaces;

import com.shop.exceptions.InvalidInputException;
import com.shop.exceptions.OutOfStockException;
import com.shop.exceptions.ProductNotFoundException;
import com.shop.models.Product;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Defines all operations that can be performed on the cart.
 * CartService must implement all these methods.
 */
public interface CartOperations {

    // add item to cart
    void addToCart(String productId, int quantity)
            throws ProductNotFoundException,
            OutOfStockException,
            InvalidInputException;

    // remove item from cart
    void removeFromCart(String productId)
            throws ProductNotFoundException;

    // view all items in cart
    void viewCart();

    // calculate total price of cart
    double calculateTotal();

    // clear all items from cart
    void clearCart();

    // checkout and place order
    void checkout()
            throws InvalidInputException;

    // get all available products
    ArrayList<Product> getAllProducts();

    // display all available products
    void displayAllProducts();

    // get cart items
    HashMap<String, Integer> getCartItems();

    // get total number of items in cart
    int getTotalItemsInCart();

    // check if cart is empty
    boolean isCartEmpty();
}