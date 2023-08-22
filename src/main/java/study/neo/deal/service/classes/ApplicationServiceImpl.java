package study.neo.deal.service.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.neo.deal.dto.ApplicationStatusHistoryDTO;
import study.neo.deal.dto.LoanApplicationRequestDTO;
import study.neo.deal.model.Application;
import study.neo.deal.model.Client;
import study.neo.deal.enumeration.ApplicationStatus;
import study.neo.deal.enumeration.ChangeType;
import study.neo.deal.dto.Passport;
import study.neo.deal.repository.ApplicationRepository;
import study.neo.deal.repository.ClientRepository;
import study.neo.deal.service.interfaces.ApplicationService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {
    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional
    @Override
    public Application createApplication(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Создаем клиента");
        Client client = createClient(loanApplicationRequestDTO);
        log.info("Создаем заявку");
        return createApplication(client);
    }

    private Client createClient(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Passport passport = new Passport();
        passport.setSeries(loanApplicationRequestDTO.getPassportSeries());
        passport.setNumber(loanApplicationRequestDTO.getPassportNumber());
        log.info("Заполняем паспортные данные: {}", passport);
        Client client = Client.builder()
                .firstName(loanApplicationRequestDTO.getFirstName())
                .lastName(loanApplicationRequestDTO.getLastName())
                .middleName(loanApplicationRequestDTO.getMiddleName())
                .email(loanApplicationRequestDTO.getEmail())
                .birthDate(loanApplicationRequestDTO.getBirthDate())
                .passport(passport)
                .build();
        log.info("Созданный клиент: {}", client);
        return clientRepository.save(client);
    }

    private Application createApplication(Client client) {
        ApplicationStatusHistoryDTO applicationStatusHistoryDTO = ApplicationStatusHistoryDTO.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();
        log.info("Создаем историю статусов заявки: {}", applicationStatusHistoryDTO);
        Application application = Application.builder()
                .client(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .statusHistory(List.of(applicationStatusHistoryDTO))
                .build();
        log.info("Созданная заявка: {}", application);
        Application applicationFromDb = applicationRepository.save(application);
        log.info("Id созданной заявки: {}", applicationFromDb.getApplicationId());
        return applicationFromDb;
    }
}
