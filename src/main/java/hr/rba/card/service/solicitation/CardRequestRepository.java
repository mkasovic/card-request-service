package hr.rba.card.service.solicitation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRequestRepository extends JpaRepository<CardRequest, Long> {
    Optional<CardRequest> findByOib(String oib);
    List<CardRequest> findByOibStartsWith(String oib);
}
