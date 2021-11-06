package com.epam.training.ticketservice.ui.shell;

import org.jline.utils.AttributedString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TicketPromptProviderTest {
    @Test
    public void testGetPromptShouldReturnProperShellPrompt() {
        // Given
        var ticketPromptProvider = new TicketPromptProvider();
        var expected = new AttributedString("Ticket service>");
        // When
        var actualPromp = ticketPromptProvider.getPrompt();
        // Then
        Assertions.assertEquals(expected, actualPromp);
    }
}
