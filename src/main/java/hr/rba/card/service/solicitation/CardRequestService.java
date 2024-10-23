package hr.rba.card.service.solicitation;

import hr.rba.card.service.events.CardStatusChangedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service @Transactional
@Slf4j
public class CardRequestService {
    private final CardRequestRepository repository;
    private final RestClient restClient = RestClient.create();

    @Value("${validation-service.base-path}")
    private String validationServiceBasePath;

    @Value("${validation-service.username}")
    private String validationServiceUsername;

    @Value("${validation-service.password}")
    private String validationServicePassword;

    public CardRequestService(CardRequestRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "card_status_changed", autoStartup = "true")
    public void onEvent(@Payload CardStatusChangedEvent payload,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_KEY) String key,
                       Acknowledgment ack) {
        log.info("Received event {}", payload);

        Optional<CardRequest> result = repository.findByOib(payload.getOib());
        if (result.isEmpty()) {
            log.warn("Silently skipping processing of event {}", payload);
            return;
        }

        CardRequest cardRequest = result.get();
        cardRequest.setCardStatus(CardStatus.valueOf(payload.getCardStatus()));
        repository.save(cardRequest);

        ack.acknowledge();
    }

    public List<CardRequest> findByOibStartsWith(String oib) {
        return repository.findByOibStartsWith(oib);
    }

    public CardRequest findByOib(String oib) {
        return repository.findByOib(oib).orElseThrow(() -> new CardRequestNotFoundException(oib));
    }

    public CardRequest save(CardRequest cardRequest) {
        if (repository.findByOib(cardRequest.getOib()).isPresent()) {
            throw new CardRequestAlreadyExistsException(cardRequest.getOib());
        }

        cardRequest = repository.save(cardRequest);

        log.info("Submitting new card request for validation {}", cardRequest);
        ResponseEntity<Void> response = restClient.post()
                .uri(validationServiceBasePath + "/card-request-validations")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString((validationServiceUsername + ":" + validationServicePassword).getBytes()))
                .body(cardRequest)
                .retrieve()
                .toBodilessEntity();

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to submit card request for validation");
        }

        return cardRequest;
    }

    public void deleteByOib(String oib) {
        CardRequest cardRequest = repository.findByOib(oib).orElseThrow(() -> new CardRequestNotFoundException(oib));
        repository.delete(cardRequest);
    }
}
