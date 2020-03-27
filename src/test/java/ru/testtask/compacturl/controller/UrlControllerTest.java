package ru.testtask.compacturl.controller;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.AfterEach;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UrlControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private CompactUrlService service;

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
        Url url = service.addUrl("http://google.com");

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
                .body(new UrlDTO("http://google"))
                .when()
                .post("/")
                .then()
                .statusCode(400)
                .body("message", Is.is("Invalid url"));
    }

}