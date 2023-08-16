package study.neo.deal.entities;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.neo.deal.dtos.EmploymentDTO;
import study.neo.deal.enums.*;
import study.neo.deal.models.Passport;
import study.neo.deal.repositories.ClientRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Тест сущности Client.")
public class ClientTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    @Transactional
    @DisplayName("Тест создания сущности Client.")
    public void testSaveClient() {
        Passport passport = Passport.builder()
                .issueBranch("Saratov")
                .series("1234")
                .number("123456")
                .build();

        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentINN("1234567890 123")
                .employmentPosition(EmploymentPosition.MID_MANAGER)
                .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                .workExperienceCurrent(3)
                .salary(BigDecimal.valueOf(15000))
                .workExperienceTotal(13)
                .build();

        Client client = new Client();
        client.setFirstName("firstName");
        client.setLastName("lastName");
        client.setMiddleName("middleName");
        client.setBirthDate(LocalDate.of(1999, 5, 18));
        client.setEmail("clientsEmail@mail.ru");
        client.setGender(Gender.MALE);
        client.setMaritalStatus(MaritalStatus.SINGLE);
        client.setDependentAmount(0);
        client.setEmployment(employmentDTO);
        client.setAccount("1234567890");
        client.setPassport(passport);

        entityManager.persist(client);
        clientRepository.save(client);

        List<Client> clients = entityManager.createQuery("SELECT c FROM Client c", Client.class)
                .getResultList();
        assertEquals(clients.size(), 1);
        assertEquals(clients.get(0).getFirstName(), "firstName");
        assertEquals(clients.get(0).getLastName(), "lastName");
        assertEquals(clients.get(0).getMiddleName(), "middleName");
        assertEquals(clients.get(0).getBirthDate(), LocalDate.of(1999, 5, 18));
        assertEquals(clients.get(0).getEmail(), "clientsEmail@mail.ru");
        assertEquals(clients.get(0).getGender(), Gender.MALE);
        assertEquals(clients.get(0).getMaritalStatus(), MaritalStatus.SINGLE);
        assertEquals(clients.get(0).getDependentAmount(), 0);
        assertEquals(clients.get(0).getEmployment(), employmentDTO);
        assertEquals(clients.get(0).getAccount(), "1234567890");
        assertEquals(clients.get(0).getPassport(), passport);
    }

    @Test
    @Transactional
    @DisplayName("Тест обновления сущности Client.")
    public void testUpdateApplication() {
        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentINN("1234567890 123")
                .employmentPosition(EmploymentPosition.MID_MANAGER)
                .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                .workExperienceCurrent(3)
                .salary(BigDecimal.valueOf(15000))
                .workExperienceTotal(13)
                .build();

        Client client = new Client();
        client.setFirstName("firstName");
        client.setLastName("lastName");
        client.setMiddleName("middleName");
        client.setBirthDate(LocalDate.of(1999, 5, 18));
        client.setEmail("clientsEmail@mail.ru");
        client.setGender(Gender.MALE);
        client.setMaritalStatus(MaritalStatus.SINGLE);
        client.setDependentAmount(0);
        client.setEmployment(employmentDTO);
        client.setAccount("1234567890");

        entityManager.persist(client);
        clientRepository.save(client);

        client.setFirstName("firstNameUpdated");

        clientRepository.save(client);

        Client updatedClient = entityManager.find(Client.class, client.getClientId());
        assertEquals(updatedClient.getFirstName(), "firstNameUpdated");
    }
}