package ru.testtask.compacturl.util;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class IdGenerator {
    private static final int MODULO = 1000;
    AtomicInteger counter = new AtomicInteger();

    public String generate() {
        final int counterValue = counter.getAndUpdate(operand -> (operand + 1) % MODULO);
        final long id = System.currentTimeMillis() + counterValue;
        return Converter.convert(id);
    }
}
