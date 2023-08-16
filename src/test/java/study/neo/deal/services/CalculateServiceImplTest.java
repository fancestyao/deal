package study.neo.deal.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.neo.deal.dtos.ApplicationStatusHistoryDTO;
import study.neo.deal.dtos.FinishRegistrationRequestDTO;
import study.neo.deal.dtos.LoanOfferDTO;
import study.neo.deal.entities.Application;
import study.neo.deal.entities.Client;
import study.neo.deal.entities.Credit;
import study.neo.deal.enums.ApplicationStatus;
import study.neo.deal.models.Passport;
import study.neo.deal.repositories.ApplicationRepository;
import study.neo.deal.services.classes.CalculateServiceImpl;
import study.neo.deal.services.interfaces.FeignConveyorClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест сервиса CalculateServiceImpl.")
public class CalculateServiceImplTest {
    @Mock
    private ApplicationRepository applicationRepository;
    @InjectMocks
    private CalculateServiceImpl calculateService;
    @Mock
    private FeignConveyorClient feignConveyorClient;

    @Test
    @DisplayName("Тестирование метода configureCalculation.")
    public void whenConfigureCalculationWithValidData_thenReturnApplication() {
        Passport passport = new Passport();
        passport.setIssueDate(LocalDate.of(2023, 5, 18));
        passport.setIssueBranch("Saratov");
        passport.setSeries("1234");
        passport.setNumber("123456");
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = new FinishRegistrationRequestDTO();

        ApplicationStatusHistoryDTO applicationStatusHistoryDTO = ApplicationStatusHistoryDTO.builder()
                .status(ApplicationStatus.APPROVED)
                .build();

        List<ApplicationStatusHistoryDTO> statusHistoryDTOS = new ArrayList<>();
        statusHistoryDTOS.add(applicationStatusHistoryDTO);

        Client client = new Client();
        client.setFirstName("firstName");
        client.setMiddleName("middleName");
        client.setLastName("lastName");
        client.setBirthDate(LocalDate.of(1999, 5, 18));
        client.setEmail("clientsEmail@mail.ru");
        client.setPassport(passport);

        Credit credit = new Credit();
        credit.setAmount(BigDecimal.valueOf(100000));
        credit.setTerm(10);
        credit.setMonthlyPayment(BigDecimal.valueOf(15000));
        credit.setRate(BigDecimal.valueOf(10));
        credit.setPsk(BigDecimal.valueOf(6));
        credit.setInsuranceEnable(Boolean.TRUE);
        credit.setSalaryClient(Boolean.TRUE);

        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setApplicationId(1L);
        loanOfferDTO.setTotalAmount(BigDecimal.valueOf(100000));
        loanOfferDTO.setTerm(12);
        loanOfferDTO.setRate(BigDecimal.valueOf(10));

        Application application = Application.builder()
                .client(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .credit(credit)
                .sesCode(1234)
                .appliedOffer(loanOfferDTO)
                .statusHistory(statusHistoryDTOS)
                .creationDate(LocalDateTime.of(2023, 8, 15, 1, 30, 0))
                .build();

        when(applicationRepository.findById(anyLong())).thenReturn(Optional.ofNullable(application));

        calculateService.configureCalculation(finishRegistrationRequestDTO, 1L);

        verify(applicationRepository, times(1)).findById(any());
        verify(feignConveyorClient, times(1)).sendCalculation(any());
    }
}