package ru.testtask.compacturl.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Document(collection = "url")
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Url {
    private String id;
    @NotEmpty
    @Indexed(direction = IndexDirection.ASCENDING, unique = true)
    private String url;
}
