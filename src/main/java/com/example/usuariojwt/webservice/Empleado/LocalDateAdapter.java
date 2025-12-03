package com.example.soap.infrastructure.soap.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

    @Override
    public LocalDate unmarshal(String dateString) throws Exception {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString, formatter);
    }

    @Override
    public String marshal(LocalDate date) throws Exception {
        if (date == null) {
            return null;
        }
        return formatter.format(date);
    }
}
