package ru.testtask.compacturl.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Document(collection = "url")
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Url {
    private String id;
    @NotEmpty
    private String url;
    @NotEmpty
    private String idempotenceKey;
}
