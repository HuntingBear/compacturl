package ru.testtask.compacturl.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Idempotence-Key header is missing")
public class IdempotenceKeyHeaderMissing extends RuntimeException {
}
