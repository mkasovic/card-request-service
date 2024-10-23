package hr.rba.card.service.solicitation;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class CardRequest {
    @Id @GeneratedValue
    private Long id;

    @NotNull(message = "Field 'firstName' is mandatory") @Size(min = 3, max = 25, message = "Field 'firstName' should be between 3 and 25 characters length")
    private String firstName;

    @NotNull(message = "Field 'lastName' is mandatory") @Size(min = 3, max = 25, message = "Field 'lastName' should be between 3 and 25 characters length")
    private String lastName;

    @NotNull(message = "Field 'oib' is mandatory") @Size(min = 11, max = 11, message = "Field 'oib' should be 11 characters length (numeric values only)")
    private String oib;

    @Enumerated(EnumType.STRING) @NotNull(message = "Field 'cardStatus' is mandatory")
    private CardStatus cardStatus = CardStatus.REQUESTED;
}
