package com.shop;

import com.shop.menu.MenuHandler;

/**
 * Entry point of the Online Shopping Cart System.
 * Creates a MenuHandler and starts the system.
 */
public class Main {

    public static void main(String[] args) {

        // create menu handler and start
        MenuHandler menuHandler = new MenuHandler();
        menuHandler.start();
    }
}