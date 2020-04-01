package ru.testtask.compacturl.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.testtask.compacturl.repository.UrlRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CompactUrlServiceTest {
    private final static Logger log = LoggerFactory.getLogger(CompactUrlServiceTest.class);
    private static final String GOOGLE = "http://google.com";
    private static final int URLS_NUMBER = 100;

    @Autowired
    private CompactUrlService compactUrlService;

    @Autowired
    private UrlRepository urlRepository;

    @Test
    void addAndFindSuccess() {
        var compactUrl = compactUrlService.atomicAddUrl(GOOGLE, "idempotenceKey");
        assertEquals(GOOGLE, compactUrl.getUrl());
        var originalUrl = compactUrlService.findById(compactUrl.getId());
        assertEquals(GOOGLE, originalUrl.get().getUrl());
    }

    @Test
    void addUrlsInParallel() throws InterruptedException {
        urlRepository.deleteAll();
        Runnable newUrl = () -> compactUrlService.atomicAddUrl(GOOGLE, "idempotenceKey");

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < URLS_NUMBER; i++) {
            executorService.submit(newUrl);
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(1, urlRepository.findAll().size());
        urlRepository.deleteAll();
    }
}