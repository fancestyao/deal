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
    public void sendEmailToDossier(Long applicationId, Theme theme) {
        log.info("Достаем из БД заявку с id: " + applicationId);
        Application application = getApplicationFromDB(applicationId);
        log.info("Собираем emailMessage");
        EmailMessage emailMessage = EmailMessage.builder()
                .applicationId(applicationId)
                .address(application.getClient().getEmail())
                .sesCode(application.getSesCode())
                .theme(theme)
                .build();
        log.info("Наполненное EmailMessage: {}", emailMessage);
        log.info("Отправляем запрос на MC Dossier");
        kafkaDealSender.sendMessage(emailMessage, theme.toString().toLowerCase().replace("_", "-"));
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
