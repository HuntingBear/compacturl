package ru.testtask.compacturl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.testtask.compacturl.domain.Url;

import java.util.Optional;

public interface UrlRepository extends MongoRepository<Url, String> {
    @RestResource(exported = false)
    @Override
    Page<Url> findAll(Pageable pageable);

    @RestResource(exported = false)
    @Override
    Optional<Url> findById(String s);

    @RestResource(exported = false)
    @Override
    <S extends Url> S save(S entity);

    @RestResource(exported = false)
    @Override
    <S extends Url> S insert(S s);

    @RestResource(exported = false)
    @Override
    void delete(Url url);
}
