package ru.testtask.compacturl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.testtask.compacturl.domain.Url;
import ru.testtask.compacturl.repository.UrlRepository;
import ru.testtask.compacturl.util.IdGenerator;

import java.util.List;
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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public synchronized Url addUrl(String originalUrl) {
        List<Url> urls = urlRepository.findUrlByUrl(originalUrl);
        Optional<Url> optionalUrl= urls.stream().findAny();
        return optionalUrl.orElseGet(() -> generateIdAndSave(originalUrl));
    }

    private Url generateIdAndSave(String originalUrl) {
        String generatedId = idGenerator.generate();
        Url url = new Url(generatedId, originalUrl);
        return urlRepository.save(url);
    }

    public Optional<Url> findById(String s) {
        return urlRepository.findById(s);
    }

}
