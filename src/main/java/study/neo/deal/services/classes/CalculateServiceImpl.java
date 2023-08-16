package study.neo.deal.services.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import study.neo.deal.dtos.FinishRegistrationRequestDTO;
import study.neo.deal.dtos.ScoringDataDTO;
import study.neo.deal.entities.Application;
import study.neo.deal.exceptions.NotFoundException;
import study.neo.deal.repositories.ApplicationRepository;
import study.neo.deal.services.interfaces.CalculateService;
import study.neo.deal.services.interfaces.FeignConveyorClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalculateServiceImpl implements CalculateService {
    private final FeignConveyorClient feignConveyorClient;
    private final ApplicationRepository applicationRepository;

    @Override
    public void configureCalculation(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        log.info("Достаем из БД заявку.");
        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Такой заявки не существует."));
        log.info("Рассматриваемая заявка: {}", application);
        ScoringDataDTO scoringDataDTO = fillScoringDataDTO(finishRegistrationRequestDTO, application);
        log.info("Насыщаем данными ScoringDataDTO с помощью Client из application: {}" +
                " и FinisRegistrationRequestDTO: {}", application.getClient(), finishRegistrationRequestDTO);
        log.info("Отправляем Post-запрос на МС Conveyor");
        feignConveyorClient.sendCalculation(scoringDataDTO);
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
