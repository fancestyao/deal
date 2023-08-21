package study.neo.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.neo.deal.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
