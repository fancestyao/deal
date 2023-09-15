package study.neo.deal.service.classes;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.neo.deal.dto.*;
import study.neo.deal.enumeration.ApplicationStatus;
import study.neo.deal.enumeration.ChangeType;
import study.neo.deal.enumeration.CreditStatus;
import study.neo.deal.enumeration.Theme;
import study.neo.deal.model.Application;
import study.neo.deal.exception.NotFoundException;
import study.neo.deal.model.Credit;
import study.neo.deal.repository.ApplicationRepository;
import study.neo.deal.repository.ClientRepository;
import study.neo.deal.repository.CreditRepository;
import study.neo.deal.service.interfaces.CalculateService;
import study.neo.deal.service.interfaces.FeignConveyorClient;
import study.neo.deal.service.interfaces.KafkaService;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalculateServiceImpl implements CalculateService {
    private final KafkaService kafkaService;
    private final FeignConveyorClient feignConveyorClient;
    private final ApplicationRepository applicationRepository;
    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;

    @Override
    @Transactional
    public void configureCalculation(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        log.info("Достаем из БД заявку с id: " + applicationId);
        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Заявки с id: " +
                        applicationId + " не существует."));
        log.info("Рассматриваемая заявка: {}", application);
        log.info("Насыщаем Client {} из Application с помощью FinishRegistrationRequestDTO", application.getClient());
        fillClient(finishRegistrationRequestDTO, application);
        log.info("Сохраняем Client {} из Application в репозиторий", application.getClient());
        clientRepository.save(application.getClient());
        log.info("Обновленный Client {}", application.getClient());
        log.info("Насыщаем данными ScoringDataDTO с помощью Client из application: {}" +
                " и FinisRegistrationRequestDTO: {}", application.getClient(), finishRegistrationRequestDTO);
        ScoringDataDTO scoringDataDTO = fillScoringDataDTO(application);
        log.info("Отправляем Post-запрос на МС Conveyor");
        CreditDTO creditDTO = null;
        try {
            creditDTO = feignConveyorClient.getCalculation(scoringDataDTO);
        } catch (FeignException.FeignClientException.Conflict e) {
            EmailMessage emailMessage = EmailMessage.builder()
                    .applicationId(applicationId)
                    .address(application.getClient().getEmail())
                    .theme(Theme.APPLICATION_DENIED)
                    .build();
            kafkaService.sendConflictEmail(emailMessage);
        }
        if (creditDTO != null) {
            log.info("Получаем CreditDTO с MC Conveyor: {}", creditDTO);
            log.info("Создаем Credit на основании CreditDTO");
            Credit credit = Credit.builder()
                    .monthlyPayment(creditDTO.getMonthlyPayment())
                    .creditStatus(CreditStatus.CALCULATED)
                    .amount(creditDTO.getAmount())
                    .psk(creditDTO.getPsk())
                    .term(creditDTO.getTerm())
                    .insuranceEnable(creditDTO.getIsInsuranceEnabled())
                    .salaryClient(creditDTO.getIsSalaryClient())
                    .paymentSchedule(creditDTO.getPaymentSchedule())
                    .build();
            creditRepository.save(credit);
            log.info("Созданный Credit: {}", credit);
            ApplicationStatusHistoryDTO applicationStatusHistoryDTO = ApplicationStatusHistoryDTO.builder()
                    .status(ApplicationStatus.CC_APPROVED)
                    .time(LocalDateTime.now())
                    .changeType(ChangeType.AUTOMATIC)
                    .build();
            log.info("Добавляем Credit: {} в Application: {}", credit, application);
            application.setCredit(credit);
            log.info("Добавляем в заявку статус: {}", ApplicationStatus.CC_APPROVED);
            application.setStatus(ApplicationStatus.CC_APPROVED);
            log.info("Добавляем в заявку историю статусов: {}", applicationStatusHistoryDTO);
            application.getStatusHistory().add(applicationStatusHistoryDTO);
            log.info("Измененная заявка: {}", application);
            applicationRepository.save(application);
            log.info("Заявка успешно изменена и добавлена в БД.");
            EmailMessage emailMessage = EmailMessage.builder()
                    .applicationId(applicationId)
                    .address(application.getClient().getEmail())
                    .theme(Theme.CREATE_DOCUMENTS)
                    .build();
            log.info("Наполненное EmailMessage: {}", emailMessage);
            log.info("Отправляем запрос на Dossier");
            kafkaService.sendDocumentsEmail(applicationId);
        }
    }

    private void fillClient(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Application application) {
        Passport passport = Passport.builder()
                .issueBranch(finishRegistrationRequestDTO.getPassportIssueBranch())
                .issueDate(finishRegistrationRequestDTO.getPassportIssueDate())
                .number(application.getClient().getPassport().getNumber())
                .series(application.getClient().getPassport().getSeries())
                .build();
        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentINN(finishRegistrationRequestDTO.getEmployment().getEmploymentINN())
                .employmentPosition(finishRegistrationRequestDTO.getEmployment().getEmploymentPosition())
                .employmentStatus(finishRegistrationRequestDTO.getEmployment().getEmploymentStatus())
                .salary(finishRegistrationRequestDTO.getEmployment().getSalary())
                .workExperienceCurrent(finishRegistrationRequestDTO.getEmployment().getWorkExperienceCurrent())
                .workExperienceTotal(finishRegistrationRequestDTO.getEmployment().getWorkExperienceTotal())
                .build();
        application.getClient().setGender(finishRegistrationRequestDTO.getGender());
        application.getClient().setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
        application.getClient().setPassport(passport);
        application.getClient().setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
        application.getClient().setEmployment(employmentDTO);
        application.getClient().setAccount(finishRegistrationRequestDTO.getAccount());
        application.getClient().setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus());
    }

    private ScoringDataDTO fillScoringDataDTO(Application application) {
        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .gender(application.getClient().getGender())
                .maritalStatus(application.getClient().getMaritalStatus())
                .dependentAmount(application.getClient().getDependentAmount())
                .passportIssueDate(application.getClient().getPassport().getIssueDate())
                .passportIssueBranch(application.getClient().getPassport().getIssueBranch())
                .employmentDTO(application.getClient().getEmployment())
                .account(application.getClient().getAccount())
                .firstName(application.getClient().getFirstName())
                .lastName(application.getClient().getLastName())
                .middleName(application.getClient().getMiddleName())
                .birthDate(application.getClient().getBirthDate())
                .passportSeries(application.getClient().getPassport().getSeries())
                .passportNumber(application.getClient().getPassport().getNumber())
                .amount(application.getAppliedOffer().getTotalAmount())
                .term(application.getAppliedOffer().getTerm())
                .isInsuranceEnabled(application.getAppliedOffer().getIsInsuranceEnabled())
                .isSalaryClient(application.getAppliedOffer().getIsSalaryClient())
                .build();
        log.info("Насыщенное данными ScoringDataDTO: {}", scoringDataDTO);
        return scoringDataDTO;
    }
}
