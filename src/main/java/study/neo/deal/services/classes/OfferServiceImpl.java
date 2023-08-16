package study.neo.deal.services.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.neo.deal.dtos.ApplicationStatusHistoryDTO;
import study.neo.deal.dtos.LoanOfferDTO;
import study.neo.deal.entities.Application;
import study.neo.deal.enums.ApplicationStatus;
import study.neo.deal.enums.ChangeType;
import study.neo.deal.exceptions.NotFoundException;
import study.neo.deal.repositories.ApplicationRepository;
import study.neo.deal.services.interfaces.OfferService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferServiceImpl implements OfferService {
    private final ApplicationRepository applicationRepository;

    @Override
    public void configureOffer(LoanOfferDTO loanOfferDTO) {
        log.info("Достаем из БД заявку.");
        Application application = applicationRepository
                .findById(loanOfferDTO.getApplicationId())
                .orElseThrow(() -> new NotFoundException("Такой заявки не существует."));
        log.info("Рассматриваемая заявка: {}", application);
        ApplicationStatusHistoryDTO applicationStatusHistoryDTO = ApplicationStatusHistoryDTO.builder()
                .status(ApplicationStatus.APPROVED)
                .time(LocalDateTime.now())
                .changeType(ChangeType.MANUAL)
                .build();
        log.info("Добавляем в заявку статус: {}", ApplicationStatus.APPROVED);
        application.setStatus(ApplicationStatus.APPROVED);
        log.info("Добавляем в заявку историю статусов: {}", applicationStatusHistoryDTO);
        application.getStatusHistory().add(applicationStatusHistoryDTO);
        log.info("Добавляем в заявку LoanOfferDTO: {}", loanOfferDTO);
        application.setAppliedOffer(loanOfferDTO);
        log.info("Измененная заявка: {}", application);
        applicationRepository.save(application);
        log.info("Заявка успешно изменена и добавлена в БД.");
    }
}
