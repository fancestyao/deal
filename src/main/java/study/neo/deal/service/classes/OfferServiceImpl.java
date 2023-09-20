package study.neo.deal.service.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import study.neo.deal.dto.ApplicationStatusHistoryDTO;
import study.neo.deal.dto.LoanOfferDTO;
import study.neo.deal.enumeration.Theme;
import study.neo.deal.model.Application;
import study.neo.deal.enumeration.ApplicationStatus;
import study.neo.deal.enumeration.ChangeType;
import study.neo.deal.exception.NotFoundException;
import study.neo.deal.repository.ApplicationRepository;
import study.neo.deal.service.interfaces.KafkaService;
import study.neo.deal.service.interfaces.OfferService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferServiceImpl implements OfferService {
    private final ApplicationRepository applicationRepository;
    private final KafkaService kafkaService;
    @Value("${kafka.tn.finish-registration}")
    private String finishRegistrationValue;

    @Override
    public void configureOffer(LoanOfferDTO loanOfferDTO) {
        log.info("Достаем из БД заявку с id: " + loanOfferDTO.getApplicationId());
        Application application = applicationRepository
                .findById(loanOfferDTO.getApplicationId())
                .orElseThrow(() -> new NotFoundException("Заявки с id: " +
                        loanOfferDTO.getApplicationId() + " не существует."));
        log.info("Рассматриваемая заявка: {}", application);
        ApplicationStatusHistoryDTO applicationStatusHistoryDTO = ApplicationStatusHistoryDTO.builder()
                .status(ApplicationStatus.APPROVED)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
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
        log.info("Отправляем emailMessage на MC Dossier (finish_registration) с помощью kafkaService");
        kafkaService.sendEmailToDossier(application.getApplicationId(),
                Theme.FINISH_REGISTRATION, finishRegistrationValue);
    }
}
