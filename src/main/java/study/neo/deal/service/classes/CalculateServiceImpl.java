package study.neo.deal.service.classes;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.neo.deal.dto.*;
import study.neo.deal.enumeration.ApplicationStatus;
import study.neo.deal.enumeration.CreditStatus;
import study.neo.deal.enumeration.Theme;
import study.neo.deal.model.Application;
import study.neo.deal.exception.NotFoundException;
import study.neo.deal.model.Credit;
import study.neo.deal.repository.ApplicationRepository;
import study.neo.deal.repository.ClientRepository;
import study.neo.deal.repository.CreditRepository;
import study.neo.deal.service.interfaces.ApplicationService;
import study.neo.deal.service.interfaces.CalculateService;
import study.neo.deal.service.interfaces.FeignConveyorClient;
import study.neo.deal.service.interfaces.KafkaService;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalculateServiceImpl implements CalculateService {
    private final KafkaService kafkaService;
    private final FeignConveyorClient feignConveyorClient;
    private final ApplicationRepository applicationRepository;
    private final ApplicationService applicationService;
    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;
    @Value("${kafka.tn.application-denied}")
    private String applicationDeniedValue;
    @Value("${kafka.tn.create-documents}")
    private String createDocumentsValue;

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
        CreditDTO creditDTO = null;
        try {
            log.info("Отправляем Post-запрос на МС Conveyor");
            creditDTO = feignConveyorClient.getCalculation(scoringDataDTO);
        } catch (FeignException.FeignClientException.Conflict e) {
            applicationService.updateApplicationStatus(applicationId, ApplicationStatus.CC_DENIED);
            log.info("Отправляем emailMessage на MC Dossier (application_denied) с помощью kafkaService");
            kafkaService.sendEmailToDossier(applicationId, Theme.APPLICATION_DENIED, applicationDeniedValue);
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
            applicationService.updateApplicationStatus(applicationId, ApplicationStatus.CC_APPROVED);
            log.info("Отправляем emailMessage на MC Dossier (create_documents) с помощью kafkaService");
            kafkaService.sendEmailToDossier(applicationId, Theme.CREATE_DOCUMENTS, createDocumentsValue);
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
