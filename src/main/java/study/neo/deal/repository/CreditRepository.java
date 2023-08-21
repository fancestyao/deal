package study.neo.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.neo.deal.model.Credit;

public interface CreditRepository extends JpaRepository<Credit, Long> {
}
