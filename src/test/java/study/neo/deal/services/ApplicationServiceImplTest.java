package study.neo.deal.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.neo.deal.dtos.ApplicationStatusHistoryDTO;
import study.neo.deal.dtos.LoanApplicationRequestDTO;
import study.neo.deal.dtos.LoanOfferDTO;
import study.neo.deal.entities.Application;
import study.neo.deal.entities.Client;
import study.neo.deal.entities.Credit;
import study.neo.deal.enums.ApplicationStatus;
import study.neo.deal.repositories.ApplicationRepository;
import study.neo.deal.repositories.ClientRepository;
import study.neo.deal.services.classes.ApplicationServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест сервиса ApplicationServiceImpl.")
public class ApplicationServiceImplTest {
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private ClientRepository clientRepository;
    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Test
    @DisplayName("Тестирование метода configureApplication.")
    public void whenConfigureOfferWithValidData_thenReturnApplication() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO();
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

        Credit credit = new Credit();
        credit.setAmount(BigDecimal.valueOf(100000));
        credit.setTerm(10);
        credit.setMonthlyPayment(BigDecimal.valueOf(15000));
        credit.setRate(BigDecimal.valueOf(10));
        credit.setPsk(BigDecimal.valueOf(6));
        credit.setInsuranceEnable(Boolean.TRUE);
        credit.setSalaryClient(Boolean.TRUE);

        Application application = Application.builder()
                .client(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .credit(credit)
                .sesCode(1234)
                .statusHistory(statusHistoryDTOS)
                .creationDate(LocalDateTime.of(2023, 8, 15, 1, 30, 0))
                .build();

        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setApplicationId(1L);
        loanOfferDTO.setTotalAmount(BigDecimal.valueOf(100000));
        loanOfferDTO.setTerm(12);
        loanOfferDTO.setRate(BigDecimal.valueOf(10));

        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        applicationService.configureApplication(loanApplicationRequestDTO);

        verify(applicationRepository, times(1)).save(any());
        verify(clientRepository, times(1)).save(any());
    }
}
