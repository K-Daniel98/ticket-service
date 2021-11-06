package com.epam.training.ticketservice.core.pricing.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PriceComponentModelTest {

    @Test
    public void testEqualsBasedOnPriceShouldReturnTrue() {
        // Given
        var basePrice = new BasePrice(1500L);
        var otherBasePrice = new BasePrice(1500L);
        // When
        var result = basePrice.equals(otherBasePrice);
        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testEqualsBasedOnPriceShouldReturnFalse() {
        // Given
        var basePrice = new BasePrice(1500L);
        var otherBasePrice = new BasePrice(1900L);
        // When
        var result = basePrice.equals(otherBasePrice);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentTypesShouldReturnFalse() {
        // Given
        var basePrice = new BasePrice(1500L);
        var otherType = "a string";
        // When
        var result = basePrice.equals(otherType);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testEqualsSameObjectShouldReturnTrue() {
        // Given
        var basePrice = new BasePrice(1500L);
        var otherBasePrice = basePrice;
        // When
        var result = basePrice.equals(otherBasePrice);
        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testEqualsWithNullObject() {
        // Given
        var basePrice = new BasePrice(1500L);
        BasePrice otherBasePrice = null;
        // When
        var result = basePrice.equals(otherBasePrice);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testHashCodeShouldReturnTheSameValueBasedOnPriceComponentName() {
        // Given
        var basePrice = new BasePrice(1500L);
        var otherBasePrice = new BasePrice(1500L);
        // When
        var hashCode = basePrice.hashCode();
        var otherHashCode = otherBasePrice.hashCode();
        // Then
        Assertions.assertEquals(hashCode, otherHashCode);
    }

    @Test
    public void testSetAmountShouldSetValue() {
        // Given
        var basePrice = new BasePrice(1500L);
        var newAmount = 2950L;
        // When
        basePrice.setAmount(newAmount);
        // Then
        Assertions.assertEquals(newAmount, basePrice.getAmount());
    }

}
