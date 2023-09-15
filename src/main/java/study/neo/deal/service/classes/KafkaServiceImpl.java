package study.neo.deal.service.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.neo.deal.dto.EmailMessage;
import study.neo.deal.enumeration.Theme;
import study.neo.deal.exception.NotFoundException;
import study.neo.deal.model.Application;
import study.neo.deal.repository.ApplicationRepository;
import study.neo.deal.service.interfaces.KafkaService;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaServiceImpl implements KafkaService {
    private final ApplicationRepository applicationRepository;
    private final KafkaDealSender kafkaDealSender;

    @Override
    public void sendDocumentsEmail(Long applicationId) {
        log.info("Достаем из БД заявку с id: " + applicationId);
        Application application = getApplicationFromDB(applicationId);
        log.info("Наполняем EmailMessage");
        EmailMessage emailMessage = EmailMessage.builder()
                .address(application.getClient().getEmail())
                .applicationId(application.getApplicationId())
                .theme(Theme.SEND_DOCUMENTS)
                .build();
        log.info("Наполненное EmailMessage: {}", emailMessage);
        log.info("Отправляем запрос на Dossier");
        kafkaDealSender.sendMessage(emailMessage, "send-documents");
    }

    @Override
    public void signDocumentsEmail(Long applicationId) {
        log.info("Достаем из БД заявку с id: " + applicationId);
        Application application = getApplicationFromDB(applicationId);
        EmailMessage emailMessage = EmailMessage.builder()
                .address(application.getClient().getEmail())
                .applicationId(application.getApplicationId())
                .sesCode(application.getSesCode())
                .theme(Theme.SEND_SES)
                .build();
        log.info("Наполненное EmailMessage: {}", emailMessage);
        log.info("Отправляем запрос на Dossier");
        kafkaDealSender.sendMessage(emailMessage, "send-ses");
    }

    @Override
    public void codeDocumentsEmail(Long applicationId) {
        log.info("Достаем из БД заявку с id: " + applicationId);
        Application application = getApplicationFromDB(applicationId);
        EmailMessage emailMessage = EmailMessage.builder()
                .address(application.getClient().getEmail())
                .applicationId(application.getApplicationId())
                .theme(Theme.CREDIT_ISSUED)
                .build();
        log.info("Наполненное EmailMessage: {}", emailMessage);
        log.info("Отправляем запрос на Dossier");
        kafkaDealSender.sendMessage(emailMessage, "credit-issued");
    }

    @Override
    public void sendConflictEmail(EmailMessage emailMessage) {
        log.info("Наполненное EmailMessage: {}", emailMessage);
        log.info("Отправляем запрос на Dossier");
        kafkaDealSender.sendMessage(emailMessage, "application-denied");
    }

    private Application getApplicationFromDB(Long applicationId) {
        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Заявки с id: " +
                        applicationId + " не существует."));
        log.info("Рассматриваемая заявка: {}", application);
        return application;
    }
}
