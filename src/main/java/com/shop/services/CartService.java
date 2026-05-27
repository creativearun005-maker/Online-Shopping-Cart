package com.shop.services;

import com.shop.exceptions.InvalidInputException;
import com.shop.exceptions.OutOfStockException;
import com.shop.exceptions.ProductNotFoundException;
import com.shop.interfaces.CartOperations;
import com.shop.models.Product;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Handles all cart related operations.
 * Uses ArrayList to store available products.
 * Uses HashMap to store cart items.
 * Implements CartOperations interface.
 */
public class CartService implements CartOperations {

    // ArrayList to store all available products in shop
    private ArrayList<Product> products;

    // HashMap to store cart items
    // key   = productId
    // value = quantity added to cart
    private HashMap<String, Integer> cart;

    // Constructor
    public CartService() {
        this.products = new ArrayList<>();
        this.cart     = new HashMap<>();
        loadProducts();
    }

    /**
     * Preloads products into the system.
     * In a real system this data would come from a database.
     */
    private void loadProducts() {

        // Electronics category
        products.add(new Product("P001", "Laptop",        45000.0, 10, "Electronics"));
        products.add(new Product("P002", "Smartphone",    15000.0, 25, "Electronics"));
        products.add(new Product("P003", "Headphones",     2500.0, 50, "Electronics"));
        products.add(new Product("P004", "Keyboard",       1500.0, 30, "Electronics"));
        products.add(new Product("P005", "Mouse",           800.0, 40, "Electronics"));

        // Clothing category
        products.add(new Product("P006", "T-Shirt",         500.0, 100, "Clothing"));
        products.add(new Product("P007", "Jeans",          1200.0,  60, "Clothing"));
        products.add(new Product("P008", "Jacket",         2500.0,  30, "Clothing"));

        // Books category
        products.add(new Product("P009", "Java Programming", 450.0, 20, "Books"));
        products.add(new Product("P010", "Data Structures",  350.0, 15, "Books"));

        // Food category
        products.add(new Product("P011", "Coffee",   250.0, 200, "Food"));
        products.add(new Product("P012", "Biscuits",  50.0, 500, "Food"));
    }

    /**
     * Adds a product to the cart.
     * Validates product existence and stock availability.
     */
    @Override
    public void addToCart(String productId, int quantity)
            throws ProductNotFoundException,
            OutOfStockException,
            InvalidInputException {

        // check if quantity is valid
        if (quantity <= 0) {
            throw new InvalidInputException(
                    "Quantity must be greater than zero."
            );
        }

        // find product in the list
        Product product = findProduct(productId);

        // check if enough stock is available
        if (product.getStockQuantity() < quantity) {
            throw new OutOfStockException(
                    "Only " + product.getStockQuantity() +
                            " items available for " + product.getProductName() + "."
            );
        }

        // check if product already in cart
        if (cart.containsKey(productId)) {

            // get existing quantity in cart
            int existingQuantity = cart.get(productId);
            int newQuantity      = existingQuantity + quantity;

            // check if total quantity exceeds stock
            if (newQuantity > product.getStockQuantity()) {
                throw new OutOfStockException(
                        "Cannot add " + quantity + " more items." +
                                " Only " + (product.getStockQuantity() - existingQuantity) +
                                " more items available."
                );
            }

            // update quantity in cart
            cart.put(productId, newQuantity);

        } else {
            // add new product to cart
            cart.put(productId, quantity);
        }

        System.out.println(product.getProductName() +
                " x" + quantity +
                " added to cart successfully.");
    }

    /**
     * Removes a product from the cart.
     */
    @Override
    public void removeFromCart(String productId)
            throws ProductNotFoundException {

        // check if product exists
        Product product = findProduct(productId);

        // check if product is in cart
        if (!cart.containsKey(productId)) {
            throw new ProductNotFoundException(
                    product.getProductName() +
                            " is not in your cart."
            );
        }

        // remove from cart
        cart.remove(productId);
        System.out.println(product.getProductName() +
                " removed from cart successfully.");
    }

    /**
     * Displays all items currently in the cart.
     */
    @Override
    public void viewCart() {

        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("==============================");
        System.out.println("          Your Cart          ");
        System.out.println("==============================");

        double total = 0;
        int itemNumber = 1;

        // iterate over cart items using HashMap
        for (HashMap.Entry<String, Integer> entry : cart.entrySet()) {

            String productId = entry.getKey();
            int    quantity  = entry.getValue();

            try {
                Product product   = findProduct(productId);
                double  itemTotal = product.getPrice() * quantity;
                total            += itemTotal;

                System.out.println(itemNumber + ". " + product.getProductName());
                System.out.println("   Product ID : " + productId);
                System.out.println("   Price      : Rs." + product.getPrice());
                System.out.println("   Quantity   : " + quantity);
                System.out.println("   Subtotal   : Rs." + itemTotal);
                System.out.println("   ----------------------------");

                itemNumber++;

            } catch (ProductNotFoundException e) {
                System.out.println("Product not found : " + productId);
            }
        }

        System.out.println("==============================");
        System.out.println("Total Items : " + cart.size());
        System.out.println("Total Price : Rs." + total);
        System.out.println("==============================");
    }

    /**
     * Calculates total price of all items in cart.
     */
    @Override
    public double calculateTotal() {

        double total = 0;

        for (HashMap.Entry<String, Integer> entry : cart.entrySet()) {
            String productId = entry.getKey();
            int    quantity  = entry.getValue();

            try {
                Product product = findProduct(productId);
                total += product.getPrice() * quantity;
            } catch (ProductNotFoundException e) {
                System.out.println("Product not found : " + productId);
            }
        }

        return total;
    }

    /**
     * Clears all items from the cart.
     */
    @Override
    public void clearCart() {

        if (cart.isEmpty()) {
            System.out.println("Cart is already empty.");
            return;
        }

        cart.clear();
        System.out.println("Cart cleared successfully.");
    }

    /**
     * Processes checkout.
     * Shows order summary and clears cart.
     */
    @Override
    public void checkout() throws InvalidInputException {

        if (cart.isEmpty()) {
            throw new InvalidInputException(
                    "Cannot checkout. Your cart is empty."
            );
        }

        double total = calculateTotal();

        System.out.println("==============================");
        System.out.println("       Order Summary         ");
        System.out.println("==============================");

        // show all items
        for (HashMap.Entry<String, Integer> entry : cart.entrySet()) {
            String productId = entry.getKey();
            int    quantity  = entry.getValue();

            try {
                Product product   = findProduct(productId);
                double  itemTotal = product.getPrice() * quantity;

                System.out.println(product.getProductName() +
                        " x" + quantity +
                        " = Rs." + itemTotal);

                // reduce stock after checkout
                product.setStockQuantity(
                        product.getStockQuantity() - quantity
                );

            } catch (ProductNotFoundException e) {
                System.out.println("Product not found : " + productId);
            }
        }

        System.out.println("==============================");
        System.out.println("Total Amount : Rs." + total);
        System.out.println("==============================");
        System.out.println("Order placed successfully!");
        System.out.println("Thank you for shopping with us!");

        // clear cart after checkout
        cart.clear();
    }

    /**
     * Returns list of all available products.
     */
    @Override
    public ArrayList<Product> getAllProducts() {
        return products;
    }

    /**
     * Displays all available products in the shop.
     */
    @Override
    public void displayAllProducts() {

        System.out.println("==============================");
        System.out.println("      Available Products     ");
        System.out.println("==============================");

        String currentCategory = "";

        for (Product product : products) {

            // print category header when category changes
            if (!product.getCategory().equals(currentCategory)) {
                currentCategory = product.getCategory();
                System.out.println();
                System.out.println("--- " + currentCategory + " ---");
                System.out.println();
            }

            System.out.println("ID     : " + product.getProductId());
            System.out.println("Name   : " + product.getProductName());
            System.out.println("Price  : Rs." + product.getPrice());
            System.out.println("Stock  : " + product.getStockQuantity() + " items");
            System.out.println("----------------------------");
        }
    }

    /**
     * Returns cart HashMap.
     * Used in test cases.
     */
    @Override
    public HashMap<String, Integer> getCartItems() {
        return cart;
    }

    /**
     * Returns total number of items in cart.
     */
    @Override
    public int getTotalItemsInCart() {
        return cart.size();
    }

    /**
     * Checks if cart is empty.
     */
    @Override
    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    /**
     * Finds a product by product ID.
     * Throws exception if product is not found.
     */
    private Product findProduct(String productId)
            throws ProductNotFoundException {

        for (Product product : products) {
            if (product.getProductId().equalsIgnoreCase(productId.trim())) {
                return product;
            }
        }

        throw new ProductNotFoundException(
                "Product with ID " + productId +
                        " does not exist in the shop."
        );
    }
}