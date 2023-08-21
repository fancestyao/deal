package study.neo.deal.service.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import study.neo.deal.dto.ApplicationStatusHistoryDTO;
import study.neo.deal.dto.CreditDTO;
import study.neo.deal.dto.FinishRegistrationRequestDTO;
import study.neo.deal.dto.ScoringDataDTO;
import study.neo.deal.enumeration.ApplicationStatus;
import study.neo.deal.enumeration.ChangeType;
import study.neo.deal.enumeration.CreditStatus;
import study.neo.deal.model.Application;
import study.neo.deal.exception.NotFoundException;
import study.neo.deal.model.Credit;
import study.neo.deal.repository.ApplicationRepository;
import study.neo.deal.repository.CreditRepository;
import study.neo.deal.service.interfaces.CalculateService;
import study.neo.deal.service.interfaces.FeignConveyorClient;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalculateServiceImpl implements CalculateService {
    private final FeignConveyorClient feignConveyorClient;
    private final ApplicationRepository applicationRepository;
    private final CreditRepository creditRepository;

    @Override
    public void configureCalculation(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        log.info("Достаем из БД заявку с id: " + applicationId);
        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Заявки с id: " +
                        applicationId + " не существует."));
        log.info("Рассматриваемая заявка: {}", application);
        ScoringDataDTO scoringDataDTO = fillScoringDataDTO(finishRegistrationRequestDTO, application);
        log.info("Насыщаем данными ScoringDataDTO с помощью Client из application: {}" +
                " и FinisRegistrationRequestDTO: {}", application.getClient(), finishRegistrationRequestDTO);
        log.info("Отправляем Post-запрос на МС Conveyor");
        CreditDTO creditDTO = feignConveyorClient.getCalculation(scoringDataDTO);
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
        log.info("Добавляем в заявку статус: {}", ApplicationStatus.CC_APPROVED);
        application.setStatus(ApplicationStatus.CC_APPROVED);
        log.info("Добавляем в заявку историю статусов: {}", applicationStatusHistoryDTO);
        application.getStatusHistory().add(applicationStatusHistoryDTO);
        log.info("Измененная заявка: {}", application);
        applicationRepository.save(application);
        log.info("Заявка успешно изменена и добавлена в БД.");
    }

    private ScoringDataDTO fillScoringDataDTO(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                              Application application) {
        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .gender(finishRegistrationRequestDTO.getGender())
                .maritalStatus(finishRegistrationRequestDTO.getMaritalStatus())
                .dependentAmount(finishRegistrationRequestDTO.getDependentAmount())
                .passportIssueDate(finishRegistrationRequestDTO.getPassportIssueDate())
                .passportIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch())
                .employmentDTO(finishRegistrationRequestDTO.getEmployment())
                .account(finishRegistrationRequestDTO.getAccount())
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
