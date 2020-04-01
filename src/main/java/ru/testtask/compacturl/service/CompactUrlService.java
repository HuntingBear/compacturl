package ru.testtask.compacturl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.testtask.compacturl.domain.Url;
import ru.testtask.compacturl.repository.UrlRepository;
import ru.testtask.compacturl.util.IdGenerator;

import java.util.Optional;

@Service
public class CompactUrlService {
    private UrlRepository urlRepository;

    private IdGenerator idGenerator;

    @Autowired
    public CompactUrlService(UrlRepository urlRepository, IdGenerator idGenerator) {
        this.urlRepository = urlRepository;
        this.idGenerator = idGenerator;
    }

    @Transactional
    public synchronized Url atomicAddUrl(String originalUrl) {
        String generatedId = idGenerator.generate();
        return urlRepository.addUrlIfNotFound(generatedId, originalUrl);
    }

    public Optional<Url> findById(String s) {
        return urlRepository.findById(s);
    }

}
