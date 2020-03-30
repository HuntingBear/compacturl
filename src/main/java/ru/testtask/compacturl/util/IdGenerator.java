package ru.testtask.compacturl.util;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class IdGenerator {
    private static final int BOUND = 1000;

    public String generate() {
        final int randomValue = ThreadLocalRandom.current().nextInt(0, BOUND);
        final long id = Long.parseLong("" + randomValue + System.currentTimeMillis());
        return Converter.convert(id);
    }
}
