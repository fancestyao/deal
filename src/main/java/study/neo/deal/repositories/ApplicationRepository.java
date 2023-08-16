package study.neo.deal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import study.neo.deal.entities.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}