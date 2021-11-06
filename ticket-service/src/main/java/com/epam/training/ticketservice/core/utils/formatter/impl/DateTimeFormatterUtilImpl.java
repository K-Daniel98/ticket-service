package com.epam.training.ticketservice.core.utils.formatter.impl;

import com.epam.training.ticketservice.core.utils.formatter.DateTimeFormatterUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateTimeFormatterUtilImpl implements DateTimeFormatterUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public LocalDateTime fromString(String dateStr) {
        return LocalDateTime.parse(dateStr, formatter);
    }

    @Override
    public String fromLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(formatter);
    }
}
