package com.epam.training.ticketservice.core.pricing.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BasePriceModelTest {

    @Test
    public void testEqualsBasedOnPriceComponentNameShouldReturnTrue() {
        // Given
        var priceComponent = new PriceComponent("discount", 150L);
        var otherPriceComponent = new PriceComponent("discount", 150L);
        // When
        var result = priceComponent.equals(otherPriceComponent);
        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testEqualsBasedOnPriceComponentNameShouldReturnFalse() {
        // Given
        var priceComponent = new PriceComponent("discount", 150L);
        var otherPriceComponent = new PriceComponent("discount -150 HUF", 150L);
        // When
        var result = priceComponent.equals(otherPriceComponent);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentTypesShouldReturnFalse() {
        // Given
        var priceComponent = new PriceComponent("discount", 150L);
        var otherType = "a string";
        // When
        var result = priceComponent.equals(otherType);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testEqualsSameObjectShouldReturnTrue() {
        // Given
        var priceComponent = new PriceComponent("discount", 150L);
        var otherPriceComponent = priceComponent;
        // When
        var result = priceComponent.equals(otherPriceComponent);
        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testEqualsWithNullObject() {
        // Given
        var priceComponent = new PriceComponent("discount", 150L);
        PriceComponent otherPriceComponent = null;
        // When
        var result = priceComponent.equals(otherPriceComponent);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testHashCodeShouldReturnTheSameValueBasedOnPriceComponentName() {
        // Given
        var priceComponent = new PriceComponent("discount", 150L);
        var otherPriceComponent = new PriceComponent("discount", 150L);
        // When
        var hashCode = priceComponent.hashCode();
        var otherHashCode = otherPriceComponent.hashCode();
        // Then
        Assertions.assertEquals(hashCode, otherHashCode);
    }


    @Test
    public void testSetNameShouldSetValue() {
        // Given
        var priceComponent = new PriceComponent("discount", 150L);
        var newName = "Discount 100%";
        // When
        priceComponent.setName(newName);
        // Then
        Assertions.assertEquals(newName, priceComponent.getName());
    }

    @Test
    public void testSetAmountShouldSetValue() {
        // Given
        var priceComponent = new PriceComponent("discount", 150L);
        var newAmount = 300L;
        // When
        priceComponent.setAmount(newAmount);
        // Then
        Assertions.assertEquals(newAmount, priceComponent.getAmount());
    }

}
