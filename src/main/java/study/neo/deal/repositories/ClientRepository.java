package study.neo.deal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import study.neo.deal.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
