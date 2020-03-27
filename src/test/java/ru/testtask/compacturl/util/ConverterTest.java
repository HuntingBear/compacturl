package ru.testtask.compacturl.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static ru.testtask.compacturl.util.Converter.convert;
import static ru.testtask.compacturl.util.Converter.restore;

class ConverterTest {

    @Test
    void convertId() {
        long id = 100L;
        String convertedId = convert(id);
        assertEquals("c1", convertedId);
    }

    @Test
    void convertZeroId() {
        long id = 0L;
        String convertedId = convert(id);
        assertEquals("0", convertedId);
    }

    @Test
    void restoreId() {
        String convertedId = "P";
        long id = restore(convertedId);
        assertEquals(25, id);
    }

    @Test
    void convertAndRestore() {
        long id = 123456789L;
        long result = restore(convert(id));
        assertEquals(123456789, result);
    }

}