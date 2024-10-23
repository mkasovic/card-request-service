package hr.rba.card.service.solicitation;

public class CardRequestAlreadyExistsException extends RuntimeException {
    public CardRequestAlreadyExistsException(String oib) {
        super(String.format("Card request for client with OIB %s already exists", oib));
    }
}
