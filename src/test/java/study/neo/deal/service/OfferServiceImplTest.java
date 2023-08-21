package study.neo.deal.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.neo.deal.dto.ApplicationStatusHistoryDTO;
import study.neo.deal.dto.LoanOfferDTO;
import study.neo.deal.model.Application;
import study.neo.deal.model.Client;
import study.neo.deal.model.Credit;
import study.neo.deal.enumeration.ApplicationStatus;
import study.neo.deal.exception.NotFoundException;
import study.neo.deal.repository.ApplicationRepository;
import study.neo.deal.service.classes.OfferServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест сервиса OfferServiceImpl.")
public class OfferServiceImplTest {
    @Mock
    private ApplicationRepository applicationRepository;
    @InjectMocks
    private OfferServiceImpl offerService;

    @Test
    @DisplayName("Тестирование метода configureOffer с валидными данными.")
    public void whenConfigureOfferWithValidData_thenReturnApplication() {
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

        when(applicationRepository.findById(anyLong())).thenReturn(Optional.ofNullable(application));
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        offerService.configureOffer(loanOfferDTO);

        verify(applicationRepository, times(1)).findById(any());
        verify(applicationRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Тестирование метода configureOffer с невалидными данными.")
    public void whenConfigureOfferWithInvalidData_thenThrowNotFoundExceptionForApplication() {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setApplicationId(-1L);
        loanOfferDTO.setTotalAmount(BigDecimal.valueOf(100000));
        loanOfferDTO.setTerm(12);
        loanOfferDTO.setRate(BigDecimal.valueOf(10));

        when(applicationRepository.findById(-1L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> offerService.configureOffer(loanOfferDTO));

        verify(applicationRepository, times(1)).findById(-1L);
    }
}