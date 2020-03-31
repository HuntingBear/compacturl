package ru.testtask.compacturl.controller;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import ru.testtask.compacturl.domain.Url;
import ru.testtask.compacturl.dto.UrlDTO;
import ru.testtask.compacturl.repository.UrlRepository;
import ru.testtask.compacturl.service.CompactUrlService;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UrlControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private CompactUrlService service;

    @BeforeEach
    void init() {
        urlRepository.deleteAll();
    }

    @AfterEach
    void clear() {
        urlRepository.deleteAll();
    }

    @Test
    void requestNotExistingUrl() {
        expect().statusCode(400)
                .given()
                .port(port)
                .get("/100");
    }

    @Test
    void requestExistingUrl() {
        Url url = service.addUrl("http://google.com", "idempotenceKey");

        expect().statusCode(200)
                .given()
                .port(port)
                .when()
                .get("/" + url.getId())
                .then()
                .body(Matchers.containsString("<title>Google</title>"));
    }

    @Test
    void requestMakeCompactUrl() {
        given().port(port)
                .contentType(ContentType.JSON)
                .header("Idempotence-Key", "idempotenceKey")
                .body(new UrlDTO("http://google.com"))
                .when()
                .post("/")
                .then()
                .statusCode(200)
                .body("url", Matchers.notNullValue());
    }

    @Test
    void requestMakeInvalidUrl() {
        given().port(port)
                .contentType(ContentType.JSON)
                .header("Idempotence-Key", "idempotenceKey")
                .body(new UrlDTO("http://google"))
                .when()
                .post("/")
                .then()
                .statusCode(400)
                .body("message", Is.is("Invalid url"));
    }

    @Test
    void requestMakeSameUrlTwiceWithSameIdempotenceKey() {
        String compactUrl_1 = given()
                .port(port)
                .contentType(ContentType.JSON)
                .header("Idempotence-Key", "idempotenceKey")
                .body(new UrlDTO("http://google.com"))
                .when()
                .post("/")
                .then()
                .statusCode(200)
                .body("url", Matchers.notNullValue())
                .extract()
                .body()
                .jsonPath()
                .get("url");

        String compactUrl_2 = given()
                .port(port)
                .contentType(ContentType.JSON)
                .header("Idempotence-Key", "idempotenceKey")
                .body(new UrlDTO("http://google.com"))
                .when()
                .post("/")
                .then()
                .statusCode(200)
                .body("url", Matchers.notNullValue())
                .extract()
                .body()
                .jsonPath()
                .get("url");

        assertEquals(compactUrl_1, compactUrl_2);
    }

    @Test
    void requestMakeSameUrlTwiceWithDifferentIdempotenceKey() {
        String compactUrl_1 = given()
                .port(port)
                .contentType(ContentType.JSON)
                .header("Idempotence-Key", "key1")
                .body(new UrlDTO("http://google.com"))
                .when()
                .post("/")
                .then()
                .statusCode(200)
                .body("url", Matchers.notNullValue())
                .extract()
                .body()
                .jsonPath()
                .get("url");

        String compactUrl_2 = given()
                .port(port)
                .contentType(ContentType.JSON)
                .header("Idempotence-Key", "key2")
                .body(new UrlDTO("http://google.com"))
                .when()
                .post("/")
                .then()
                .statusCode(200)
                .body("url", Matchers.notNullValue())
                .extract()
                .body()
                .jsonPath()
                .get("url");

        assertNotEquals(compactUrl_1, compactUrl_2);
    }

/*
    @Test
    void requestMakeDifferentUrlWithSameIdempotenceKey() {
        String compactUrl_1 = given()
                .port(port)
                .contentType(ContentType.JSON)
                .header("Idempotence-Key", "idempotenceKey")
                .body(new UrlDTO("http://google.com"))
                .when()
                .post("/")
                .then()
                .statusCode(200)
                .body("url", Matchers.notNullValue())
                .extract()
                .body()
                .jsonPath()
                .get("url");

        String compactUrl_2 = given()
                .port(port)
                .contentType(ContentType.JSON)
                .header("Idempotence-Key", "idempotenceKey")
                .body(new UrlDTO("http://ya.ru"))
                .when()
                .post("/")
                .then()
                .statusCode(200)
                .body("url", Matchers.notNullValue())
                .extract()
                .body()
                .jsonPath()
                .get("url");

        assertNotEquals(compactUrl_1, compactUrl_2);
    }
*/

    @Test
    void requestWithEmptyIdempotenceKeyHeader() {
        given().port(port)
                .contentType(ContentType.JSON)
                .header("Idempotence-Key", "")
                .body(new UrlDTO("http://google"))
                .when()
                .post("/")
                .then()
                .statusCode(400)
                .body("message", Is.is("Idempotence-Key header is missing"));
    }

}