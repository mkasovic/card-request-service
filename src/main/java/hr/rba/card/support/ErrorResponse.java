package hr.rba.card.support;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ErrorResponse {
    // Unique identifier for audit purposes
    private UUID id = UUID.randomUUID();

    // Optional error code for reporting purposes
    private String code;

    // The description of the error
    private String description;

    // Time when the error occurred
    private String timestamp = Instant.now().toString();

    public ErrorResponse(String description) {
        this(description,null);
    }

    public ErrorResponse(String description, String code) {
        this.code = code;
        this.description = description;
    }
}
