package com.shop.services;

import com.shop.exceptions.InvalidInputException;
import com.shop.exceptions.OutOfStockException;
import com.shop.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService();
    }

    // ── addToCart ──────────────────────────────────────────

    @Test
    @DisplayName("addToCart() - valid product and quantity should add to cart")
    void testAddToCartSuccess() throws Exception {
        cartService.addToCart("P001", 2);
        assertEquals(2, cartService.getCartItems().get("P001"));
    }

    @Test
    @DisplayName("addToCart() - adding same product twice should accumulate quantity")
    void testAddToCartAccumulatesQuantity() throws Exception {
        cartService.addToCart("P001", 2);
        cartService.addToCart("P001", 3);
        assertEquals(5, cartService.getCartItems().get("P001"));
    }

    @Test
    @DisplayName("addToCart() - invalid product ID should throw ProductNotFoundException")
    void testAddToCartProductNotFound() {
        assertThrows(ProductNotFoundException.class,
                () -> cartService.addToCart("P999", 1));
    }

    @Test
    @DisplayName("addToCart() - zero quantity should throw InvalidInputException")
    void testAddToCartZeroQuantity() {
        assertThrows(InvalidInputException.class,
                () -> cartService.addToCart("P001", 0));
    }

    @Test
    @DisplayName("addToCart() - negative quantity should throw InvalidInputException")
    void testAddToCartNegativeQuantity() {
        assertThrows(InvalidInputException.class,
                () -> cartService.addToCart("P001", -3));
    }

    @Test
    @DisplayName("addToCart() - quantity exceeding stock should throw OutOfStockException")
    void testAddToCartExceedsStock() {
        // P001 Laptop has stock 10
        assertThrows(OutOfStockException.class,
                () -> cartService.addToCart("P001", 11));
    }

    @Test
    @DisplayName("addToCart() - accumulated quantity exceeding stock should throw OutOfStockException")
    void testAddToCartAccumulatedExceedsStock() throws Exception {
        cartService.addToCart("P001", 8);
        assertThrows(OutOfStockException.class,
                () -> cartService.addToCart("P001", 5));
    }

    @Test
    @DisplayName("addToCart() - exact stock quantity should succeed")
    void testAddToCartExactStock() throws Exception {
        cartService.addToCart("P001", 10); // stock is exactly 10
        assertEquals(10, cartService.getCartItems().get("P001"));
    }

    @Test
    @DisplayName("addToCart() - lowercase product ID should work (case-insensitive)")
    void testAddToCartCaseInsensitive() throws Exception {
        cartService.addToCart("p001", 1);
        // cart key may be lowercase, just check cart is not empty
        assertFalse(cartService.isCartEmpty());
    }

    // ── removeFromCart ─────────────────────────────────────

    @Test
    @DisplayName("removeFromCart() - existing cart item should be removed")
    void testRemoveFromCartSuccess() throws Exception {
        cartService.addToCart("P001", 2);
        cartService.removeFromCart("P001");
        assertFalse(cartService.getCartItems().containsKey("P001"));
    }

    @Test
    @DisplayName("removeFromCart() - product not in cart should throw ProductNotFoundException")
    void testRemoveFromCartNotInCart() {
        assertThrows(ProductNotFoundException.class,
                () -> cartService.removeFromCart("P001"));
    }

    @Test
    @DisplayName("removeFromCart() - non-existent product ID should throw ProductNotFoundException")
    void testRemoveFromCartInvalidProduct() {
        assertThrows(ProductNotFoundException.class,
                () -> cartService.removeFromCart("P999"));
    }

    // ── calculateTotal ─────────────────────────────────────

    @Test
    @DisplayName("calculateTotal() - empty cart should return 0.0")
    void testCalculateTotalEmptyCart() {
        assertEquals(0.0, cartService.calculateTotal());
    }

    @Test
    @DisplayName("calculateTotal() - single item total should be price * quantity")
    void testCalculateTotalSingleItem() throws Exception {
        cartService.addToCart("P001", 2); // Laptop Rs.45000 x2 = 90000
        assertEquals(90000.0, cartService.calculateTotal());
    }

    @Test
    @DisplayName("calculateTotal() - multiple items should sum correctly")
    void testCalculateTotalMultipleItems() throws Exception {
        cartService.addToCart("P001", 1); // 45000
        cartService.addToCart("P002", 1); // 15000
        assertEquals(60000.0, cartService.calculateTotal());
    }

    @Test
    @DisplayName("calculateTotal() - after removing item total should decrease")
    void testCalculateTotalAfterRemove() throws Exception {
        cartService.addToCart("P001", 1); // 45000
        cartService.addToCart("P002", 1); // 15000
        cartService.removeFromCart("P001");
        assertEquals(15000.0, cartService.calculateTotal());
    }

    // ── clearCart ──────────────────────────────────────────

    @Test
    @DisplayName("clearCart() - should empty the cart")
    void testClearCart() throws Exception {
        cartService.addToCart("P001", 1);
        cartService.addToCart("P002", 2);
        cartService.clearCart();
        assertTrue(cartService.isCartEmpty());
    }

    @Test
    @DisplayName("clearCart() - clearing already empty cart should not throw")
    void testClearCartAlreadyEmpty() {
        assertDoesNotThrow(() -> cartService.clearCart());
    }

    @Test
    @DisplayName("clearCart() - total should be 0 after clearing")
    void testCalculateTotalAfterClear() throws Exception {
        cartService.addToCart("P001", 1);
        cartService.clearCart();
        assertEquals(0.0, cartService.calculateTotal());
    }

    // ── checkout ───────────────────────────────────────────

    @Test
    @DisplayName("checkout() - empty cart should throw InvalidInputException")
    void testCheckoutEmptyCart() {
        assertThrows(InvalidInputException.class,
                () -> cartService.checkout());
    }

    @Test
    @DisplayName("checkout() - should clear cart after successful checkout")
    void testCheckoutClearsCart() throws Exception {
        cartService.addToCart("P001", 1);
        cartService.checkout();
        assertTrue(cartService.isCartEmpty());
    }

    @Test
    @DisplayName("checkout() - should reduce product stock after checkout")
    void testCheckoutReducesStock() throws Exception {
        int stockBefore = cartService.getAllProducts()
                .stream()
                .filter(p -> p.getProductId().equals("P001"))
                .findFirst().get().getStockQuantity(); // 10

        cartService.addToCart("P001", 3);
        cartService.checkout();

        int stockAfter = cartService.getAllProducts()
                .stream()
                .filter(p -> p.getProductId().equals("P001"))
                .findFirst().get().getStockQuantity();

        assertEquals(stockBefore - 3, stockAfter);
    }

    @Test
    @DisplayName("checkout() - cart should be empty after checkout")
    void testCartEmptyAfterCheckout() throws Exception {
        cartService.addToCart("P002", 2);
        cartService.checkout();
        assertEquals(0, cartService.getTotalItemsInCart());
    }

    // ── isCartEmpty / getTotalItemsInCart ──────────────────

    @Test
    @DisplayName("isCartEmpty() - new cart should be empty")
    void testIsCartEmptyOnNew() {
        assertTrue(cartService.isCartEmpty());
    }

    @Test
    @DisplayName("isCartEmpty() - should return false after adding item")
    void testIsCartEmptyAfterAdd() throws Exception {
        cartService.addToCart("P001", 1);
        assertFalse(cartService.isCartEmpty());
    }

    @Test
    @DisplayName("getTotalItemsInCart() - should return correct count of distinct products")
    void testGetTotalItemsInCart() throws Exception {
        cartService.addToCart("P001", 1);
        cartService.addToCart("P002", 2);
        assertEquals(2, cartService.getTotalItemsInCart());
    }

    @Test
    @DisplayName("getTotalItemsInCart() - duplicate product should not increase count")
    void testGetTotalItemsNoDuplicate() throws Exception {
        cartService.addToCart("P001", 1);
        cartService.addToCart("P001", 1);
        assertEquals(1, cartService.getTotalItemsInCart());
    }

    // ── getAllProducts ─────────────────────────────────────

    @Test
    @DisplayName("getAllProducts() - should return 12 preloaded products")
    void testGetAllProductsCount() {
        assertEquals(12, cartService.getAllProducts().size());
    }

    @Test
    @DisplayName("getAllProducts() - should not return null")
    void testGetAllProductsNotNull() {
        assertNotNull(cartService.getAllProducts());
    }

    @Test
    @DisplayName("getAllProducts() - first product should be P001 Laptop")
    void testGetAllProductsFirstProduct() {
        assertEquals("P001", cartService.getAllProducts().get(0).getProductId());
        assertEquals("Laptop", cartService.getAllProducts().get(0).getProductName());
    }

    // ── viewCart / displayAllProducts (no-throw checks) ───

    @Test
    @DisplayName("viewCart() - should not throw on empty cart")
    void testViewCartEmpty() {
        assertDoesNotThrow(() -> cartService.viewCart());
    }

    @Test
    @DisplayName("viewCart() - should not throw with items in cart")
    void testViewCartWithItems() throws Exception {
        cartService.addToCart("P001", 1);
        assertDoesNotThrow(() -> cartService.viewCart());
    }

    @Test
    @DisplayName("displayAllProducts() - should not throw")
    void testDisplayAllProducts() {
        assertDoesNotThrow(() -> cartService.displayAllProducts());
    }
}
