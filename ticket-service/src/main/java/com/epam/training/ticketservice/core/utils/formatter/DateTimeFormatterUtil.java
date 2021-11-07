package com.epam.training.ticketservice.core.utils.formatter;

import java.time.LocalDateTime;

public interface DateTimeFormatterUtil {
    LocalDateTime fromString(String dateStr);

    String fromLocalDateTime(LocalDateTime localDateTime);
}