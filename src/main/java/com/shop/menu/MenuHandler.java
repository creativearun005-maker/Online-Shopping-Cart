package com.shop.menu;

import com.shop.exceptions.InvalidInputException;
import com.shop.exceptions.OutOfStockException;
import com.shop.exceptions.ProductNotFoundException;
import com.shop.models.Product;
import com.shop.services.CartService;
import com.shop.utils.InputValidator;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Handles all GUI related operations.
 * Creates and manages all AWT windows.
 * Main window, Products window, Cart window
 * and Checkout window.
 */
public class MenuHandler {

    /* Needed Objects */
    private CartService    cartService;
    private InputValidator inputValidator;

    /* Color Constants */
    private static final Color BLUE_DARK   = new Color(26,  115, 232);
    private static final Color BLUE_LIGHT  = new Color(208, 228, 255);
    private static final Color WHITE       = new Color(255, 255, 255);
    private static final Color TEXT_DARK   = new Color(51,  51,  51);
    private static final Color BG_COLOR    = new Color(245, 248, 255);
    private static final Color RED_COLOR   = new Color(220, 53,  69);
    private static final Color GREEN_COLOR = new Color(40,  167, 69);

    /* Font Constants */
    private static final Font FONT_TITLE   = new Font("Arial", Font.BOLD, 20);
    private static final Font FONT_HEADER  = new Font("Arial", Font.BOLD, 15);
    private static final Font FONT_NORMAL  = new Font("Arial", Font.PLAIN, 13);
    private static final Font FONT_BOLD    = new Font("Arial", Font.BOLD, 13);
    private static final Font FONT_BUTTON  = new Font("Arial", Font.BOLD, 13);

    // Constructor
    public MenuHandler() {
        this.cartService    = new CartService();
        this.inputValidator = new InputValidator();
    }

    /**
     * Starts the application by opening main window.
     */
    public void start() {
        openMainWindow();
    }

    /**
     * Creates and opens the main window.
     * Contains buttons for all main operations.
     */
    private void openMainWindow() {

        Frame mainFrame = new Frame("Online Shopping Cart");
        mainFrame.setSize(400, 500);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setBackground(BG_COLOR);
        mainFrame.setResizable(false);

        // center frame on screen
        mainFrame.setLocationRelativeTo(null);

        // header panel
        Panel headerPanel = new Panel();
        headerPanel.setBackground(BLUE_DARK);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        Label titleLabel = new Label("Online Shopping Cart");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);

        Label subtitleLabel = new Label("Your one stop shop for everything!");
        subtitleLabel.setFont(FONT_NORMAL);
        subtitleLabel.setForeground(BLUE_LIGHT);
        headerPanel.add(subtitleLabel);

        // buttons panel
        Panel buttonsPanel = new Panel();
        buttonsPanel.setLayout(new GridLayout(4, 1, 0, 20));
        buttonsPanel.setBackground(BG_COLOR);

        // create buttons
        Button viewProductsBtn = createButton("View All Products", BLUE_DARK);
        Button viewCartBtn     = createButton("View Cart", BLUE_DARK);
        Button checkoutBtn     = createButton("Checkout", GREEN_COLOR);
        Button exitBtn         = createButton("Exit", RED_COLOR);

        // add buttons to panel
        buttonsPanel.add(viewProductsBtn);
        buttonsPanel.add(viewCartBtn);
        buttonsPanel.add(checkoutBtn);
        buttonsPanel.add(exitBtn);

        // wrapper panel for buttons with padding
        Panel wrapperPanel = new Panel();
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.setBackground(BG_COLOR);
        wrapperPanel.add(buttonsPanel, BorderLayout.CENTER);

        // add panels to frame
        mainFrame.add(headerPanel,  BorderLayout.NORTH);
        mainFrame.add(wrapperPanel, BorderLayout.CENTER);

        // button actions
        viewProductsBtn.addActionListener(e -> openProductsWindow());
        viewCartBtn.addActionListener(e -> openCartWindow(mainFrame));
        checkoutBtn.addActionListener(e -> openCheckoutWindow(mainFrame));
        exitBtn.addActionListener(e -> {
            mainFrame.dispose();
            System.exit(0);
        });

        // close on X
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                mainFrame.dispose();
                System.exit(0);
            }
        });

        mainFrame.setVisible(true);
    }

    /**
     * Creates and opens the products window.
     * Shows all available products with Add to Cart buttons.
     */
    private void openProductsWindow() {

        Frame productsFrame = new Frame("All Products");
        productsFrame.setSize(700, 600);
        productsFrame.setLayout(new BorderLayout());
        productsFrame.setBackground(BG_COLOR);
        productsFrame.setResizable(false);
        productsFrame.setAlwaysOnTop(true);

        // header panel
        Panel headerPanel = new Panel();
        headerPanel.setBackground(BLUE_DARK);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        Label titleLabel = new Label("Available Products");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);

        // scroll pane for products
        ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);

        // main products panel
        Panel productsPanel = new Panel();
        productsPanel.setLayout(new GridLayout(0, 1, 0, 5));
        productsPanel.setBackground(BG_COLOR);

        ArrayList<Product> products    = cartService.getAllProducts();
        String             currentCategory = "";

        for (Product product : products) {

            // add category header when category changes
            if (!product.getCategory().equals(currentCategory)) {
                currentCategory = product.getCategory();

                Panel categoryPanel = new Panel();
                categoryPanel.setBackground(BLUE_LIGHT);
                categoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));

                Label categoryLabel = new Label("  " + currentCategory);
                categoryLabel.setFont(FONT_HEADER);
                categoryLabel.setForeground(BLUE_DARK);
                categoryPanel.add(categoryLabel);

                productsPanel.add(categoryPanel);
            }

            // create product row panel
            Panel productRow = new Panel();
            productRow.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
            productRow.setBackground(WHITE);

            // product details labels
            Label idLabel    = new Label(product.getProductId());
            Label nameLabel  = new Label(String.format("%-20s", product.getProductName()));
            Label priceLabel = new Label("Rs." + product.getPrice());
            Label stockLabel = new Label("Stock: " + product.getStockQuantity());

            idLabel.setFont(FONT_BOLD);
            nameLabel.setFont(FONT_NORMAL);
            priceLabel.setFont(FONT_BOLD);
            stockLabel.setFont(FONT_NORMAL);

            idLabel.setForeground(BLUE_DARK);
            nameLabel.setForeground(TEXT_DARK);
            priceLabel.setForeground(GREEN_COLOR);
            stockLabel.setForeground(TEXT_DARK);

            // add to cart button
            Button addBtn = createSmallButton("Add to Cart", BLUE_DARK);

            // add all to product row
            productRow.add(idLabel);
            productRow.add(nameLabel);
            productRow.add(priceLabel);
            productRow.add(stockLabel);
            productRow.add(addBtn);

            productsPanel.add(productRow);

            // add to cart button action
            addBtn.addActionListener(e ->
                    openAddToCartDialog(productsFrame, product)
            );
        }

        scrollPane.add(productsPanel);

        productsFrame.add(headerPanel, BorderLayout.NORTH);
        productsFrame.add(scrollPane,  BorderLayout.CENTER);

        // close on X
        productsFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                productsFrame.dispose();
            }
        });

        productsFrame.setVisible(true);
    }

    /**
     * Opens a dialog to add a product to cart.
     * User enters quantity and clicks Add.
     */
    private void openAddToCartDialog(Frame parent, Product product) {

        Dialog dialog = new Dialog(parent, "Add to Cart", true);
        dialog.setSize(350, 280);
        dialog.setLayout(new BorderLayout());
        dialog.setBackground(BG_COLOR);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        // header
        Panel headerPanel = new Panel();
        headerPanel.setBackground(BLUE_DARK);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        Label titleLabel = new Label("Add to Cart");
        titleLabel.setFont(FONT_HEADER);
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);

        // details panel
        Panel detailsPanel = new Panel();
        detailsPanel.setLayout(new GridLayout(5, 1, 0, 10));
        detailsPanel.setBackground(BG_COLOR);

        Label nameLabel  = new Label("Product  : " + product.getProductName());
        Label priceLabel = new Label("Price    : Rs." + product.getPrice());
        Label stockLabel = new Label("In Stock : " + product.getStockQuantity() + " items");

        nameLabel.setFont(FONT_BOLD);
        priceLabel.setFont(FONT_BOLD);
        stockLabel.setFont(FONT_NORMAL);

        nameLabel.setForeground(TEXT_DARK);
        priceLabel.setForeground(GREEN_COLOR);
        stockLabel.setForeground(TEXT_DARK);

        // quantity input
        Panel quantityPanel = new Panel();
        quantityPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        quantityPanel.setBackground(BG_COLOR);

        Label quantityLabel = new Label("Quantity :");
        quantityLabel.setFont(FONT_BOLD);
        quantityLabel.setForeground(TEXT_DARK);

        TextField quantityField = new TextField("1", 5);
        quantityField.setFont(FONT_NORMAL);

        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityField);

        // message label for errors
        Label messageLabel = new Label("");
        messageLabel.setFont(FONT_NORMAL);
        messageLabel.setForeground(RED_COLOR);

        detailsPanel.add(nameLabel);
        detailsPanel.add(priceLabel);
        detailsPanel.add(stockLabel);
        detailsPanel.add(quantityPanel);
        detailsPanel.add(messageLabel);

        // buttons panel
        Panel buttonsPanel = new Panel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonsPanel.setBackground(BG_COLOR);

        Button addBtn    = createButton("Add to Cart", BLUE_DARK);
        Button cancelBtn = createButton("Cancel",      RED_COLOR);

        buttonsPanel.add(addBtn);
        buttonsPanel.add(cancelBtn);

        dialog.add(headerPanel,  BorderLayout.NORTH);
        dialog.add(detailsPanel, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        // add button action
        addBtn.addActionListener(e -> {
            try {
                int quantity = Integer.parseInt(
                        quantityField.getText().trim()
                );

                cartService.addToCart(
                        product.getProductId(), quantity
                );

                messageLabel.setForeground(GREEN_COLOR);
                messageLabel.setText("Added successfully!");
                dialog.repaint();

                // close dialog after 1 second
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        dialog.dispose();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();

            } catch (NumberFormatException ex) {
                messageLabel.setText("Please enter a valid number!");
            } catch (OutOfStockException      |
                     ProductNotFoundException  |
                     InvalidInputException ex) {
                messageLabel.setText("Error: " + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    /**
     * Creates and opens the cart window.
     * Shows all items in cart with Remove buttons.
     */
    private void openCartWindow(Frame parent) {

        Frame cartFrame = new Frame("Your Cart");
        cartFrame.setSize(650, 550);
        cartFrame.setLayout(new BorderLayout());
        cartFrame.setBackground(BG_COLOR);
        cartFrame.setResizable(false);
        cartFrame.setAlwaysOnTop(true);

        // header panel
        Panel headerPanel = new Panel();
        headerPanel.setBackground(BLUE_DARK);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        Label titleLabel = new Label("Your Shopping Cart");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);

        // refresh cart display
        refreshCartWindow(cartFrame, headerPanel);
    }

    /**
     * Refreshes cart window content.
     * Called when items are added or removed.
     */
    private void refreshCartWindow(Frame cartFrame, Panel headerPanel) {

        cartFrame.removeAll();
        cartFrame.add(headerPanel, BorderLayout.NORTH);

        HashMap<String, Integer> cartItems = cartService.getCartItems();

        if (cartItems.isEmpty()) {

            Panel emptyPanel = new Panel();
            emptyPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 100));
            emptyPanel.setBackground(BG_COLOR);

            Label emptyLabel = new Label("Your cart is empty!");
            emptyLabel.setFont(FONT_HEADER);
            emptyLabel.setForeground(TEXT_DARK);
            emptyPanel.add(emptyLabel);

            cartFrame.add(emptyPanel, BorderLayout.CENTER);

        } else {

            ScrollPane scrollPane = new ScrollPane(
                    ScrollPane.SCROLLBARS_AS_NEEDED
            );

            Panel cartPanel = new Panel();
            cartPanel.setLayout(new GridLayout(0, 1, 0, 5));
            cartPanel.setBackground(BG_COLOR);

            double total = 0;

            for (HashMap.Entry<String, Integer> entry :
                    cartItems.entrySet()) {

                String productId = entry.getKey();
                int    quantity  = entry.getValue();

                try {
                    Product product   = cartService
                            .getAllProducts()
                            .stream()
                            .filter(p -> p.getProductId()
                                    .equals(productId))
                            .findFirst()
                            .orElse(null);

                    if (product != null) {

                        double itemTotal = product.getPrice() * quantity;
                        total           += itemTotal;

                        Panel itemRow = new Panel();
                        itemRow.setLayout(
                                new FlowLayout(FlowLayout.LEFT, 10, 10)
                        );
                        itemRow.setBackground(WHITE);

                        Label nameLabel  = new Label(
                                String.format("%-20s", product.getProductName())
                        );
                        Label qtyLabel   = new Label("x" + quantity);
                        Label totalLabel = new Label("Rs." + itemTotal);

                        nameLabel.setFont(FONT_BOLD);
                        qtyLabel.setFont(FONT_NORMAL);
                        totalLabel.setFont(FONT_BOLD);

                        nameLabel.setForeground(TEXT_DARK);
                        qtyLabel.setForeground(TEXT_DARK);
                        totalLabel.setForeground(GREEN_COLOR);

                        Button removeBtn = createSmallButton(
                                "Remove", RED_COLOR
                        );

                        itemRow.add(nameLabel);
                        itemRow.add(qtyLabel);
                        itemRow.add(totalLabel);
                        itemRow.add(removeBtn);

                        cartPanel.add(itemRow);

                        // remove button action
                        String pid = productId;
                        removeBtn.addActionListener(e -> {
                            try {
                                cartService.removeFromCart(pid);
                                refreshCartWindow(cartFrame, headerPanel);
                            } catch (ProductNotFoundException ex) {
                                System.out.println(
                                        "Error: " + ex.getMessage()
                                );
                            }
                        });
                    }

                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }

            scrollPane.add(cartPanel);
            cartFrame.add(scrollPane, BorderLayout.CENTER);

            // total and buttons panel
            Panel bottomPanel = new Panel();
            bottomPanel.setLayout(new GridLayout(3, 1, 0, 5));
            bottomPanel.setBackground(BG_COLOR);

            // total label
            Panel totalPanel = new Panel();
            totalPanel.setBackground(BLUE_LIGHT);
            totalPanel.setLayout(
                    new FlowLayout(FlowLayout.RIGHT, 20, 10)
            );

            Label totalLabel = new Label(
                    "Total : Rs." + total
            );
            totalLabel.setFont(FONT_HEADER);
            totalLabel.setForeground(BLUE_DARK);
            totalPanel.add(totalLabel);

            // buttons panel
            Panel buttonsPanel = new Panel();
            buttonsPanel.setLayout(
                    new FlowLayout(FlowLayout.CENTER, 20, 10)
            );
            buttonsPanel.setBackground(BG_COLOR);

            Button clearBtn    = createButton("Clear Cart", RED_COLOR);
            Button checkoutBtn = createButton("Checkout",   GREEN_COLOR);

            buttonsPanel.add(clearBtn);
            buttonsPanel.add(checkoutBtn);

            bottomPanel.add(totalPanel);
            bottomPanel.add(buttonsPanel);

            cartFrame.add(bottomPanel, BorderLayout.SOUTH);

            // clear cart button action
            clearBtn.addActionListener(e -> {
                cartService.clearCart();
                refreshCartWindow(cartFrame, headerPanel);
            });

            // checkout button action
            checkoutBtn.addActionListener(e -> {
                cartFrame.dispose();
                openCheckoutWindow(null);
            });
        }

        cartFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                cartFrame.dispose();
            }
        });

        cartFrame.setVisible(true);
        cartFrame.revalidate();
        cartFrame.repaint();
    }

    /**
     * Creates and opens the checkout window.
     * Shows order summary and confirm button.
     */
    private void openCheckoutWindow(Frame parent) {

        if (cartService.isCartEmpty()) {
            showMessageDialog(
                    parent,
                    "Cart is Empty",
                    "Your cart is empty. Add items before checkout!"
            );
            return;
        }

        Frame checkoutFrame = new Frame("Checkout");
        checkoutFrame.setSize(500, 550);
        checkoutFrame.setLayout(new BorderLayout());
        checkoutFrame.setBackground(BG_COLOR);
        checkoutFrame.setResizable(false);
        checkoutFrame.setAlwaysOnTop(true);

        // header panel
        Panel headerPanel = new Panel();
        headerPanel.setBackground(BLUE_DARK);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        Label titleLabel = new Label("Order Summary");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);

        // order items panel
        ScrollPane scrollPane = new ScrollPane(
                ScrollPane.SCROLLBARS_AS_NEEDED
        );

        Panel orderPanel = new Panel();
        orderPanel.setLayout(new GridLayout(0, 1, 0, 5));
        orderPanel.setBackground(BG_COLOR);

        HashMap<String, Integer> cartItems = cartService.getCartItems();
        double total = 0;

        for (HashMap.Entry<String, Integer> entry : cartItems.entrySet()) {

            String productId = entry.getKey();
            int    quantity  = entry.getValue();

            Product product = cartService
                    .getAllProducts()
                    .stream()
                    .filter(p -> p.getProductId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (product != null) {

                double itemTotal = product.getPrice() * quantity;
                total           += itemTotal;

                Panel itemRow = new Panel();
                itemRow.setLayout(
                        new FlowLayout(FlowLayout.LEFT, 15, 10)
                );
                itemRow.setBackground(WHITE);

                Label nameLabel  = new Label(
                        String.format("%-20s", product.getProductName())
                );
                Label qtyLabel   = new Label("x" + quantity);
                Label totalLabel = new Label("= Rs." + itemTotal);

                nameLabel.setFont(FONT_BOLD);
                qtyLabel.setFont(FONT_NORMAL);
                totalLabel.setFont(FONT_BOLD);

                nameLabel.setForeground(TEXT_DARK);
                qtyLabel.setForeground(TEXT_DARK);
                totalLabel.setForeground(GREEN_COLOR);

                itemRow.add(nameLabel);
                itemRow.add(qtyLabel);
                itemRow.add(totalLabel);

                orderPanel.add(itemRow);
            }
        }

        scrollPane.add(orderPanel);

        // total panel
        Panel totalPanel = new Panel();
        totalPanel.setBackground(BLUE_LIGHT);
        totalPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        Label totalLabel = new Label("Total Amount : Rs." + total);
        totalLabel.setFont(FONT_HEADER);
        totalLabel.setForeground(BLUE_DARK);
        totalPanel.add(totalLabel);

        // buttons panel
        Panel buttonsPanel = new Panel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonsPanel.setBackground(BG_COLOR);

        Button confirmBtn = createButton("Confirm Order", GREEN_COLOR);
        Button cancelBtn  = createButton("Cancel",        RED_COLOR);

        buttonsPanel.add(confirmBtn);
        buttonsPanel.add(cancelBtn);

        // bottom panel
        Panel bottomPanel = new Panel();
        bottomPanel.setLayout(new GridLayout(2, 1));
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.add(totalPanel);
        bottomPanel.add(buttonsPanel);

        checkoutFrame.add(headerPanel, BorderLayout.NORTH);
        checkoutFrame.add(scrollPane,  BorderLayout.CENTER);
        checkoutFrame.add(bottomPanel, BorderLayout.SOUTH);

        // confirm button action
        double finalTotal = total;
        confirmBtn.addActionListener(e -> {
            try {
                cartService.checkout();
                checkoutFrame.dispose();
                showOrderSuccessDialog(finalTotal);
            } catch (InvalidInputException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> checkoutFrame.dispose());

        checkoutFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                checkoutFrame.dispose();
            }
        });

        checkoutFrame.setVisible(true);
    }

    /**
     * Shows order success dialog after checkout.
     */
    private void showOrderSuccessDialog(double total) {

        Frame successFrame = new Frame("Order Placed!");
        successFrame.setSize(400, 300);
        successFrame.setLayout(new BorderLayout());
        successFrame.setBackground(BG_COLOR);
        successFrame.setResizable(false);
        successFrame.setAlwaysOnTop(true);

        // header
        Panel headerPanel = new Panel();
        headerPanel.setBackground(GREEN_COLOR);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        Label titleLabel = new Label("Order Placed Successfully!");
        titleLabel.setFont(FONT_HEADER);
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);

        // details panel
        Panel detailsPanel = new Panel();
        detailsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        detailsPanel.setBackground(BG_COLOR);

        Label messageLabel = new Label(
                "Thank you for shopping with us!"
        );
        Label totalLabel = new Label(
                "Amount Paid : Rs." + total
        );

        messageLabel.setFont(FONT_BOLD);
        totalLabel.setFont(FONT_HEADER);

        messageLabel.setForeground(TEXT_DARK);
        totalLabel.setForeground(GREEN_COLOR);

        detailsPanel.add(messageLabel);
        detailsPanel.add(totalLabel);

        // close button
        Panel buttonPanel = new Panel();
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        Button closeBtn = createButton("Close", BLUE_DARK);
        buttonPanel.add(closeBtn);

        successFrame.add(headerPanel,  BorderLayout.NORTH);
        successFrame.add(detailsPanel, BorderLayout.CENTER);
        successFrame.add(buttonPanel,  BorderLayout.SOUTH);

        closeBtn.addActionListener(e -> successFrame.dispose());

        successFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                successFrame.dispose();
            }
        });

        successFrame.setVisible(true);
    }

    /**
     * Shows a simple message dialog.
     */
    private void showMessageDialog(Frame parent,
                                   String title,
                                   String message) {

        Dialog dialog = new Dialog(
                parent != null ? parent : new Frame(),
                title,
                true
        );
        dialog.setSize(350, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setBackground(BG_COLOR);
        dialog.setResizable(false);
        dialog.setAlwaysOnTop(true);

        Panel messagePanel = new Panel();
        messagePanel.setLayout(
                new FlowLayout(FlowLayout.CENTER, 0, 40)
        );
        messagePanel.setBackground(BG_COLOR);

        Label messageLabel = new Label(message);
        messageLabel.setFont(FONT_BOLD);
        messageLabel.setForeground(TEXT_DARK);
        messagePanel.add(messageLabel);

        Panel buttonPanel = new Panel();
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setLayout(
                new FlowLayout(FlowLayout.CENTER, 0, 10)
        );

        Button okBtn = createButton("OK", BLUE_DARK);
        buttonPanel.add(okBtn);

        dialog.add(messagePanel, BorderLayout.CENTER);
        dialog.add(buttonPanel,  BorderLayout.SOUTH);

        okBtn.addActionListener(e -> dialog.dispose());

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    /**
     * Creates a styled button with given text and color.
     */
    private Button createButton(String text, Color color) {

        Button button = new Button(text);
        button.setFont(FONT_BUTTON);
        button.setBackground(color);
        button.setForeground(WHITE);
        button.setPreferredSize(new java.awt.Dimension(180, 40));
        return button;
    }

    /**
     * Creates a smaller styled button.
     */
    private Button createSmallButton(String text, Color color) {

        Button button = new Button(text);
        button.setFont(FONT_BUTTON);
        button.setBackground(color);
        button.setForeground(WHITE);
        button.setPreferredSize(new java.awt.Dimension(100, 30));
        return button;
    }
}