package study.neo.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.neo.deal.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}