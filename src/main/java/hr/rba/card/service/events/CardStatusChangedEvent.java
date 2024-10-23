package hr.rba.card.service.events;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data @NoArgsConstructor
public class CardStatusChangedEvent {
    private String correlationId = UUID.randomUUID().toString();
    private String source;
    private String timestamp = Instant.now().toString();
    private String oib;
    private String cardStatus;

    public CardStatusChangedEvent(String source, String oib, String cardStatus) {
        this.source = source;
        this.oib = oib;
        this.cardStatus = cardStatus;
    }
}
