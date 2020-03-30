package ru.testtask.compacturl.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CompactUrlServiceTest {
    private static final String GOOGLE = "http://google.com";

    @Autowired
    private CompactUrlService compactUrlService;

    @Test
    void addAndFindSuccess() {
        var compactUrl = compactUrlService.addUrl(GOOGLE, "idempotenceKey");
        assertEquals(GOOGLE, compactUrl.getUrl());
        var originalUrl = compactUrlService.findById(compactUrl.getId());
        assertEquals(GOOGLE, originalUrl.get().getUrl());
    }
}