package study.neo.deal.dto;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import study.neo.deal.enumeration.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@DisplayName("Тест FinishRegistrationRequestDTO.")
public class FinishRegistrationRequestDTOTest {
    @Autowired
    private JacksonTester<FinishRegistrationRequestDTO> jacksonTester;

    @Test
    @DisplayName("Сериализация FinishRegistrationRequestDTO.")
    void testSerialize() throws IOException {
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = FinishRegistrationRequestDTO.builder()
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(0)
                .passportIssueDate(LocalDate.of(2019, 5, 18))
                .passportIssueBranch("Saratov")
                .employment(EmploymentDTO.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .employmentINN("123412341234 123")
                        .salary(BigDecimal.valueOf(50000))
                        .workExperienceTotal(15)
                        .workExperienceCurrent(5)
                        .employmentPosition(EmploymentPosition.TOP_MANAGER)
                        .build())
                .account("1234567890")
                .build();
        JsonContent<FinishRegistrationRequestDTO> result = jacksonTester.write(finishRegistrationRequestDTO);
        assertThat(result).extractingJsonPathValue("$.gender").isEqualTo("MALE");
        assertThat(result).extractingJsonPathValue("$.maritalStatus").isEqualTo("SINGLE");
        assertThat(result).extractingJsonPathNumberValue("$.dependentAmount").isEqualTo(0);
        assertThat(result).extractingJsonPathStringValue("$.passportIssueDate")
                .isEqualTo("2019-05-18");
        assertThat(result).extractingJsonPathStringValue("$.passportIssueBranch")
                .isEqualTo("Saratov");
        assertThat(result).extractingJsonPathValue("$.employment").isEqualTo(Map.of(
                "employmentStatus", "SELF_EMPLOYED",
                "employmentINN", "123412341234 123",
                "salary", 50000,
                "workExperienceTotal", 15,
                "workExperienceCurrent", 5,
                "employmentPosition", "TOP_MANAGER"
        ));
        assertThat(result).extractingJsonPathStringValue("$.account").isEqualTo("1234567890");
    }

    @Test
    @DisplayName("Десериализация ApplicationStatusHistory.")
    void testDeserialize() throws IOException {
        String jsonString = "{\"gender\": \"MALE\"," +
                " \"maritalStatus\": \"SINGLE\"," +
                " \"dependentAmount\": \"0\"," +
                " \"passportIssueDate\": \"2019-05-18\"," +
                " \"passportIssueBranch\": \"Saratov\"," +
                " \"employment\": {" +
                " \"employmentStatus\": \"SELF_EMPLOYED\"," +
                " \"employmentINN\": \"123412341234 123\"," +
                " \"salary\": \"50000\"," +
                " \"workExperienceTotal\": \"15\"," +
                " \"workExperienceCurrent\": \"5\"," +
                " \"employmentPosition\": \"TOP_MANAGER\"}," +
                " \"account\": \"1234567890\"}";
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = jacksonTester.parseObject(jsonString);
        AssertionsForClassTypes.assertThat(finishRegistrationRequestDTO.getGender()).isEqualTo(Gender.MALE);
        AssertionsForClassTypes.assertThat(finishRegistrationRequestDTO.getMaritalStatus())
                .isEqualTo(MaritalStatus.SINGLE);
        AssertionsForClassTypes.assertThat(finishRegistrationRequestDTO.getDependentAmount())
                .isEqualTo(0);
        AssertionsForClassTypes.assertThat(finishRegistrationRequestDTO.getPassportIssueDate())
                .isEqualTo(LocalDate.of(2019, 5, 18));
        AssertionsForClassTypes.assertThat(finishRegistrationRequestDTO.getPassportIssueBranch())
                .isEqualTo("Saratov");
        AssertionsForClassTypes.assertThat(finishRegistrationRequestDTO.getEmployment())
                .isEqualTo(EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                .employmentINN("123412341234 123")
                .salary(BigDecimal.valueOf(50000))
                .workExperienceTotal(15)
                .workExperienceCurrent(5)
                .employmentPosition(EmploymentPosition.TOP_MANAGER)
                .build());
        AssertionsForClassTypes.assertThat(finishRegistrationRequestDTO.getAccount()).isEqualTo("1234567890");
    }
}