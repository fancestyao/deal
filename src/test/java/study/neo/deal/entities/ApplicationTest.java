package study.neo.deal.entities;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.neo.deal.dtos.ApplicationStatusHistoryDTO;
import study.neo.deal.dtos.LoanOfferDTO;
import study.neo.deal.enums.ApplicationStatus;
import study.neo.deal.repositories.ApplicationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Тест сущности application.")
public class ApplicationTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ApplicationRepository applicationRepository;

    @Test
    @Transactional
    @DisplayName("Тест создания сущности Application.")
    public void testSaveApplication() {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setApplicationId(1L);

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

        entityManager.persist(client);

        Application application = Application.builder()
                .client(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .credit(credit)
                .appliedOffer(loanOfferDTO)
                .sesCode(1234)
                .statusHistory(statusHistoryDTOS)
                .creationDate(LocalDateTime.of(2023, 8, 15, 1, 30, 0))
                .build();

        applicationRepository.save(application);

        List<Application> applications = entityManager.createQuery("SELECT a FROM Application a", Application.class)
                .getResultList();
        assertEquals(applications.size(), 1);
        assertEquals(applications.get(0).getClient().getFirstName(), "firstName");
        assertEquals(applications.get(0).getCredit().getTerm(), 10);
        assertEquals(applications.get(0).getCreationDate(),
                LocalDateTime.of(2023, 8, 15, 1, 30, 0));
        assertEquals(applications.get(0).getCredit().getTerm(), 10);
        assertEquals(applications.get(0).getAppliedOffer().getApplicationId(), 1L);
        assertEquals(applications.get(0).getSesCode(), 1234);
        assertEquals(applications.get(0).getStatusHistory().get(0).getStatus(), ApplicationStatus.APPROVED);
    }

    @Test
    @Transactional
    @DisplayName("Тест обновления сущности Application.")
    public void testUpdateApplication() {
        Client client = new Client();
        client.setFirstName("firstName");
        client.setMiddleName("middleName");
        client.setLastName("lastName");
        client.setBirthDate(LocalDate.of(1999, 5, 18));
        client.setEmail("clientsEmail@mail.ru");

        entityManager.persist(client);

        Application application = Application.builder()
                .client(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now())
                .build();

        applicationRepository.save(application);

        application.setStatus(ApplicationStatus.APPROVED);
        applicationRepository.save(application);

        Application updatedApplication = entityManager.find(Application.class, application.getApplicationId());
        assertEquals(updatedApplication.getStatus(), ApplicationStatus.APPROVED);
    }
}
