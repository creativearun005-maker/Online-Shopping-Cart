package com.shop.utils;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    private InputValidator validator;

    @BeforeEach
    void setUp() {
        validator = new InputValidator();
    }

    // ── isValidInput ───────────────────────────────────────

    @Test
    @DisplayName("isValidInput() - normal string should return true")
    void testValidInputNormal() {
        assertTrue(validator.isValidInput("hello"));
    }

    @Test
    @DisplayName("isValidInput() - null should return false")
    void testValidInputNull() {
        assertFalse(validator.isValidInput(null));
    }

    @Test
    @DisplayName("isValidInput() - empty string should return false")
    void testValidInputEmpty() {
        assertFalse(validator.isValidInput(""));
    }

    @Test
    @DisplayName("isValidInput() - whitespace only should return false")
    void testValidInputWhitespace() {
        assertFalse(validator.isValidInput("   "));
    }

    @Test
    @DisplayName("isValidInput() - string with spaces around content should return true")
    void testValidInputPadded() {
        assertTrue(validator.isValidInput("  hello  "));
    }

    // ── isValidQuantity ────────────────────────────────────

    @Test
    @DisplayName("isValidQuantity() - positive value should return true")
    void testValidQuantityPositive() {
        assertTrue(validator.isValidQuantity(1));
    }

    @Test
    @DisplayName("isValidQuantity() - zero should return false")
    void testValidQuantityZero() {
        assertFalse(validator.isValidQuantity(0));
    }

    @Test
    @DisplayName("isValidQuantity() - negative value should return false")
    void testValidQuantityNegative() {
        assertFalse(validator.isValidQuantity(-5));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100, 999})
    @DisplayName("isValidQuantity() - various positive values should return true")
    void testValidQuantityMultiple(int qty) {
        assertTrue(validator.isValidQuantity(qty));
    }

    // ── isValidProductId ───────────────────────────────────

    @Test
    @DisplayName("isValidProductId() - P001 should return true")
    void testValidProductIdP001() {
        assertTrue(validator.isValidProductId("P001"));
    }

    @Test
    @DisplayName("isValidProductId() - P999 should return true")
    void testValidProductIdP999() {
        assertTrue(validator.isValidProductId("P999"));
    }

    @Test
    @DisplayName("isValidProductId() - null should return false")
    void testValidProductIdNull() {
        assertFalse(validator.isValidProductId(null));
    }

    @Test
    @DisplayName("isValidProductId() - empty string should return false")
    void testValidProductIdEmpty() {
        assertFalse(validator.isValidProductId(""));
    }

    @Test
    @DisplayName("isValidProductId() - lowercase p001 should return false")
    void testValidProductIdLowercase() {
        assertFalse(validator.isValidProductId("p001"));
    }

    @Test
    @DisplayName("isValidProductId() - missing P prefix should return false")
    void testValidProductIdNoPrefix() {
        assertFalse(validator.isValidProductId("001"));
    }

    @Test
    @DisplayName("isValidProductId() - only 2 digits should return false")
    void testValidProductIdTwoDigits() {
        assertFalse(validator.isValidProductId("P01"));
    }

    @Test
    @DisplayName("isValidProductId() - 4 digits should return false")
    void testValidProductIdFourDigits() {
        assertFalse(validator.isValidProductId("P0012"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"P001", "P010", "P100", "P999"})
    @DisplayName("isValidProductId() - valid formatted IDs should return true")
    void testValidProductIdMultiple(String id) {
        assertTrue(validator.isValidProductId(id));
    }

    // ── isValidMenuChoice ──────────────────────────────────

    @Test
    @DisplayName("isValidMenuChoice() - value within range should return true")
    void testValidMenuChoiceInRange() {
        assertTrue(validator.isValidMenuChoice(3, 1, 5));
    }

    @Test
    @DisplayName("isValidMenuChoice() - min boundary should return true")
    void testValidMenuChoiceAtMin() {
        assertTrue(validator.isValidMenuChoice(1, 1, 5));
    }

    @Test
    @DisplayName("isValidMenuChoice() - max boundary should return true")
    void testValidMenuChoiceAtMax() {
        assertTrue(validator.isValidMenuChoice(5, 1, 5));
    }

    @Test
    @DisplayName("isValidMenuChoice() - below min should return false")
    void testValidMenuChoiceBelowMin() {
        assertFalse(validator.isValidMenuChoice(0, 1, 5));
    }

    @Test
    @DisplayName("isValidMenuChoice() - above max should return false")
    void testValidMenuChoiceAboveMax() {
        assertFalse(validator.isValidMenuChoice(6, 1, 5));
    }

    @Test
    @DisplayName("isValidMenuChoice() - negative value should return false")
    void testValidMenuChoiceNegative() {
        assertFalse(validator.isValidMenuChoice(-1, 1, 5));
    }

    // ── isValidPrice ───────────────────────────────────────

    @Test
    @DisplayName("isValidPrice() - positive price should return true")
    void testValidPricePositive() {
        assertTrue(validator.isValidPrice(99.99));
    }

    @Test
    @DisplayName("isValidPrice() - zero should return false")
    void testValidPriceZero() {
        assertFalse(validator.isValidPrice(0.0));
    }

    @Test
    @DisplayName("isValidPrice() - negative price should return false")
    void testValidPriceNegative() {
        assertFalse(validator.isValidPrice(-10.0));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.01, 1.0, 500.0, 99999.99})
    @DisplayName("isValidPrice() - various positive prices should return true")
    void testValidPriceMultiple(double price) {
        assertTrue(validator.isValidPrice(price));
    }

    // ── isValidAmount ──────────────────────────────────────

    @Test
    @DisplayName("isValidAmount() - positive amount should return true")
    void testValidAmountPositive() {
        assertTrue(validator.isValidAmount(500.0));
    }

    @Test
    @DisplayName("isValidAmount() - zero should return false")
    void testValidAmountZero() {
        assertFalse(validator.isValidAmount(0.0));
    }

    @Test
    @DisplayName("isValidAmount() - negative amount should return false")
    void testValidAmountNegative() {
        assertFalse(validator.isValidAmount(-100.0));
    }

    @Test
    @DisplayName("isValidAmount() - very small positive amount should return true")
    void testValidAmountSmall() {
        assertTrue(validator.isValidAmount(0.001));
    }
}
