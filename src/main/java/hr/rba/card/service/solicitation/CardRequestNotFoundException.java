package hr.rba.card.service.solicitation;

public class CardRequestNotFoundException extends RuntimeException {
    public CardRequestNotFoundException(String oib) {
        super(String.format("Card request for client with OIB %s not found", oib));
    }
}
