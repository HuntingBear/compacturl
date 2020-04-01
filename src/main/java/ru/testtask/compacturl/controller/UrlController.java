package ru.testtask.compacturl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.testtask.compacturl.dto.UrlDTO;
import ru.testtask.compacturl.exception.IdempotenceKeyHeaderMissing;
import ru.testtask.compacturl.exception.InvalidUrlException;
import ru.testtask.compacturl.service.CompactUrlService;
import ru.testtask.compacturl.util.Validator;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.ResponseEntity.status;

@RepositoryRestController
@RequestMapping
public class UrlController {
    private CompactUrlService service;

    @Autowired
    public UrlController(CompactUrlService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UrlDTO> urlCreation(@RequestHeader("Idempotence-Key") String idempotenceKey,
                                              @RequestBody UrlDTO urlDTO,
                                              UriComponentsBuilder builder) {
        if (StringUtils.isEmpty(idempotenceKey)) {
            throw new IdempotenceKeyHeaderMissing();
        }
        if (Validator.isUrlValid(urlDTO.getUrl())) {
            var compactUrl = service.atomicAddUrl(urlDTO.getUrl(), idempotenceKey);
            var uri = builder.path("/{id}").buildAndExpand(compactUrl.getId()).toUri();
            UrlDTO compactUrlDTO = new UrlDTO(uri.toString());
            return ResponseEntity.ok(compactUrlDTO);
        } else {
            throw new InvalidUrlException();
        }
    }

    @GetMapping("/{id:[a-zA-Z0-9]+}")
    public ResponseEntity<?> findUrlById(@PathVariable String id) {
        var response = service.findById(id).orElseThrow(InvalidUrlException::new);
        return status(FOUND).header(LOCATION, response.getUrl()).build();
    }
}
