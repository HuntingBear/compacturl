package ru.testtask.compacturl.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CompactUrlServiceTest {
    private static final String GOOGLE = "http://google.com";

    @Autowired
    private CompactUrlService compactUrlService;

    @Test
    void addAndFindSuccess() {
        var compactUrl = compactUrlService.atomicAddUrl(GOOGLE);
        assertEquals(GOOGLE, compactUrl.getUrl());
        var originalUrl = compactUrlService.findById(compactUrl.getId());
        assertEquals(GOOGLE, originalUrl.get().getUrl());
    }
}