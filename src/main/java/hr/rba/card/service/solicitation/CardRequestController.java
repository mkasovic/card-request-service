package hr.rba.card.service.solicitation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/card-requests")
@AllArgsConstructor
public class CardRequestController {
    private final CardRequestService service;

    @GetMapping
    public List<CardRequest> findAllByOibStartsWith(@RequestParam("oib") @Size(min = 5) String oib) {
        return service.findByOibStartsWith(oib);
    }

    @GetMapping("/{oib}")
    CardRequest findOneByOib(@PathVariable String oib) {
        return service.findByOib(oib);
    }

    @PostMapping
    public CardRequest create(@Valid @RequestBody CardRequest cardRequest) {
        return service.save(cardRequest);
    }

    @DeleteMapping("/{oib}")
    public void delete(@PathVariable String oib) {
        service.deleteByOib(oib);
    }
}
