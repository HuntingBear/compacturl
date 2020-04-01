package ru.testtask.compacturl.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.testtask.compacturl.domain.Url;

public class UrlRepositoryImpl implements UrlRepositoryCustom {

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public Url addUrlIfNotFound(String generatedId, String url) {
        Query query = new Query(Criteria.where("url").is(url));
        Update update = new Update()
                .setOnInsert("id", generatedId)
                .setOnInsert("url", url);
        return mongoOperations.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                Url.class
        );
    }
}
