package ru.testtask.compacturl.repository;

import ru.testtask.compacturl.domain.Url;

public interface UrlRepositoryCustom {
    Url addUrlIfNotFound(String generatedId, String url);
}
