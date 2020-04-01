package ru.testtask.compacturl.repository;

import ru.testtask.compacturl.domain.Url;

public interface UrlRepositoryCustom {
    Url addUrlIfKeyNotFound(String generatedId, String url, String idempotenceKey);
}