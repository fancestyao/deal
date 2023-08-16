package study.neo.deal.dtos;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import study.neo.deal.enums.EmploymentPosition;
import study.neo.deal.enums.EmploymentStatus;
import study.neo.deal.enums.Gender;
import study.neo.deal.enums.MaritalStatus;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@DisplayName("Тест ScoringDataDTO.")
public class ScoringDataDTOTest {
    @Autowired
    private JacksonTester<ScoringDataDTO> jacksonTester;

    @Test
    @DisplayName("Сериализация ScoringDataDTO.")
    void testSerialize() throws IOException {
        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(24)
                .firstName("firstName")
                .lastName("lastName")
                .middleName("middleName")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1999, 5, 18))
                .passportSeries("1234")
                .passportNumber("123456")
                .passportIssueDate(LocalDate.of(2019, 5, 18))
                .passportIssueBranch("Saratov")
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(0)
                .employmentDTO(EmploymentDTO.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .employmentINN("123412341234 123")
                        .salary(BigDecimal.valueOf(50000))
                        .workExperienceTotal(15)
                        .workExperienceCurrent(5)
                        .employmentPosition(EmploymentPosition.TOP_MANAGER)
                        .build())
                .account("1234567890")
                .isInsuranceEnabled(Boolean.TRUE)
                .isSalaryClient(Boolean.FALSE)
                .build();
        JsonContent<ScoringDataDTO> result = jacksonTester.write(scoringDataDTO);
        assertThat(result).extractingJsonPathNumberValue("$.amount").isEqualTo(100000);
        assertThat(result).extractingJsonPathNumberValue("$.term").isEqualTo(24);
        assertThat(result).extractingJsonPathStringValue("$.firstName").isEqualTo("firstName");
        assertThat(result).extractingJsonPathStringValue("$.lastName").isEqualTo("lastName");
        assertThat(result).extractingJsonPathStringValue("$.middleName").isEqualTo("middleName");
        assertThat(result).extractingJsonPathValue("$.gender").isEqualTo("MALE");
        assertThat(result).extractingJsonPathStringValue("$.birthDate").isEqualTo("1999-05-18");
        assertThat(result).extractingJsonPathStringValue("$.passportSeries").isEqualTo("1234");
        assertThat(result).extractingJsonPathStringValue("$.passportNumber").isEqualTo("123456");
        assertThat(result).extractingJsonPathStringValue("$.passportIssueDate")
                .isEqualTo("2019-05-18");
        assertThat(result).extractingJsonPathStringValue("$.passportIssueBranch")
                .isEqualTo("Saratov");
        assertThat(result).extractingJsonPathValue("$.maritalStatus").isEqualTo("SINGLE");
        assertThat(result).extractingJsonPathNumberValue("$.dependentAmount").isEqualTo(0);
        assertThat(result).extractingJsonPathValue("$.employmentDTO").isEqualTo(Map.of(
                "employmentStatus", "SELF_EMPLOYED",
                "employmentINN", "123412341234 123",
                "salary", 50000,
                "workExperienceTotal", 15,
                "workExperienceCurrent", 5,
                "employmentPosition", "TOP_MANAGER"
        ));
        assertThat(result).extractingJsonPathStringValue("$.account").isEqualTo("1234567890");
        assertThat(result).extractingJsonPathValue("$.isInsuranceEnabled").isEqualTo(Boolean.TRUE);
        assertThat(result).extractingJsonPathValue("$.isSalaryClient").isEqualTo(Boolean.FALSE);
    }

    @Test
    @DisplayName("Десериализация ScoringDataDTO.")
    void testDeserialize() throws IOException {
        String jsonString = "{\"amount\": 100000," +
                " \"term\": 24," +
                " \"firstName\": \"firstName\"," +
                " \"lastName\": \"lastName\"," +
                " \"middleName\": \"middleName\"," +
                " \"gender\": \"MALE\"," +
                " \"birthDate\": \"1999-05-18\"," +
                " \"passportSeries\": 1234," +
                " \"passportNumber\": \"123456\"," +
                " \"passportIssueDate\": \"2019-05-18\"," +
                " \"passportIssueBranch\": \"Saratov\"," +
                " \"maritalStatus\": \"SINGLE\"," +
                " \"dependentAmount\": \"0\"," +
                " \"employmentDTO\": {" +
                " \"employmentStatus\": \"SELF_EMPLOYED\"," +
                " \"employmentINN\": \"123412341234 123\"," +
                " \"salary\": \"50000\"," +
                " \"workExperienceTotal\": \"15\"," +
                " \"workExperienceCurrent\": \"5\"," +
                " \"employmentPosition\": \"TOP_MANAGER\"}," +
                " \"account\": \"1234567890\"," +
                " \"isInsuranceEnabled\": \"true\"," +
                " \"isSalaryClient\": \"false\"}";
        ScoringDataDTO scoringDataDTO = jacksonTester.parseObject(jsonString);
        AssertionsForClassTypes.assertThat(scoringDataDTO.getAmount()).isEqualTo(BigDecimal.valueOf(100000));
        AssertionsForClassTypes.assertThat(scoringDataDTO.getTerm()).isEqualTo(24);
        AssertionsForClassTypes.assertThat(scoringDataDTO.getFirstName()).isEqualTo("firstName");
        AssertionsForClassTypes.assertThat(scoringDataDTO.getLastName()).isEqualTo("lastName");
        AssertionsForClassTypes.assertThat(scoringDataDTO.getMiddleName()).isEqualTo("middleName");
        AssertionsForClassTypes.assertThat(scoringDataDTO.getGender()).isEqualTo(Gender.MALE);
        AssertionsForClassTypes.assertThat(scoringDataDTO.getBirthDate())
                .isEqualTo(LocalDate.of(1999, 5, 18));
        AssertionsForClassTypes.assertThat(scoringDataDTO.getPassportSeries()).isEqualTo("1234");
        AssertionsForClassTypes.assertThat(scoringDataDTO.getPassportNumber()).isEqualTo("123456");
        AssertionsForClassTypes.assertThat(scoringDataDTO.getPassportIssueDate())
                .isEqualTo(LocalDate.of(2019, 5, 18));
        AssertionsForClassTypes.assertThat(scoringDataDTO.getPassportIssueBranch()).isEqualTo("Saratov");
        AssertionsForClassTypes.assertThat(scoringDataDTO.getMaritalStatus()).isEqualTo(MaritalStatus.SINGLE);
        AssertionsForClassTypes.assertThat(scoringDataDTO.getDependentAmount()).isEqualTo(0);
        AssertionsForClassTypes.assertThat(scoringDataDTO.getEmploymentDTO()).isEqualTo(EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                .employmentINN("123412341234 123")
                .salary(BigDecimal.valueOf(50000))
                .workExperienceTotal(15)
                .workExperienceCurrent(5)
                .employmentPosition(EmploymentPosition.TOP_MANAGER)
                .build());
        AssertionsForClassTypes.assertThat(scoringDataDTO.getAccount()).isEqualTo("1234567890");
        AssertionsForClassTypes.assertThat(scoringDataDTO.getIsInsuranceEnabled()).isEqualTo(true);
        AssertionsForClassTypes.assertThat(scoringDataDTO.getIsSalaryClient()).isEqualTo(false);
    }
}