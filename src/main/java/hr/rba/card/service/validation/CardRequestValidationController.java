package hr.rba.card.service.validation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/card-request-validations")
@AllArgsConstructor @Slf4j
public class CardRequestValidationController {
    private CardRequestValidationService service;

    @PostMapping
    public ResponseEntity<Response> create(@Valid @RequestBody CardRequest cardRequest) {
        service.addRequestToQueue(cardRequest);
        log.info("Card request accepted for validation {}", cardRequest);

        return new ResponseEntity<>(
                new Response("Card request accepted for validation"),
                HttpStatus.ACCEPTED
        );
    }

    public record Response(String message) {
    }
}
