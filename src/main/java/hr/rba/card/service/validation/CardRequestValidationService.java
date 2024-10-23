package hr.rba.card.service.validation;

import hr.rba.card.service.events.CardStatusChangedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@AllArgsConstructor @Slf4j
public class CardRequestValidationService {
    private final Map<String, CardRequest> requests = Collections.synchronizedMap(new HashMap<String, CardRequest>());;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Random random = new Random();

    public void addRequestToQueue(CardRequest cardRequest) {
        requests.put(cardRequest.getOib(), cardRequest);
    }

    @Scheduled(fixedRate = 30000)
    protected void processRequests() {
        requests.forEach((oib, cardRequest) -> validate(cardRequest));
    }

    public CardRequest validate(CardRequest cardRequest) {
        return validate(cardRequest, true);
    }

    public CardRequest validate(CardRequest cardRequest, boolean distributeResult) {
        log.info("Processing card request for client with OIB {}", cardRequest.getOib());
        if (cardRequest.getCardStatus() == CardStatus.APPROVED || cardRequest.getCardStatus() == CardStatus.REJECTED) {
            log.info("Card request for client with OIB {} already processed", cardRequest.getOib());
            return cardRequest;
        }

        cardRequest.setCardStatus(getNextCardStatus(cardRequest.getCardStatus()));
        log.info("Card request is processed and new status is {}", cardRequest.getCardStatus());

        if (distributeResult) {
            CardStatusChangedEvent event = new CardStatusChangedEvent(getClass().getName(), cardRequest.getOib(), cardRequest.getCardStatus().toString());
            log.info("Distributing new event {}", event);
            kafkaTemplate.send("card_status_changed", event.getOib(), event);
        }

        return cardRequest;
    }

    private CardStatus getNextCardStatus(CardStatus cardStatus) {
        CardStatus nextCardStatus;
        switch (cardStatus) {
            case REQUESTED -> nextCardStatus = CardStatus.PENDING;
            case PENDING   -> nextCardStatus = getRandomResolutionCardStatus();
            default        -> nextCardStatus = cardStatus;
        }
        return nextCardStatus;
    }

    private CardStatus getRandomResolutionCardStatus() {
        CardStatus[] cardStatuses = new CardStatus[] {CardStatus.APPROVED, CardStatus.REJECTED};
        return cardStatuses[random.nextInt(cardStatuses.length)];
    }
}
