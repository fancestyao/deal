package study.neo.deal.dto;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import study.neo.deal.enumeration.EmploymentPosition;
import study.neo.deal.enumeration.EmploymentStatus;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@DisplayName("Тест EmploymentDTO.")
public class EmploymentDTOTest {
    @Autowired
    private JacksonTester<EmploymentDTO> jacksonTester;

    @Test
    @DisplayName("Сериализация EmploymentDTO.")
    void testSerialize() throws IOException {
        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                .employmentINN("123412341234 123")
                .salary(BigDecimal.valueOf(50000))
                .workExperienceTotal(15)
                .workExperienceCurrent(5)
                .employmentPosition(EmploymentPosition.TOP_MANAGER)
                .build();
        JsonContent<EmploymentDTO> result = jacksonTester.write(employmentDTO);
        assertThat(result).extractingJsonPathValue("$.employmentStatus")
                .isEqualTo("SELF_EMPLOYED");
        assertThat(result).extractingJsonPathStringValue("$.employmentINN")
                .isEqualTo("123412341234 123");
        assertThat(result).extractingJsonPathNumberValue("$.salary").isEqualTo(50000);
        assertThat(result).extractingJsonPathNumberValue("$.workExperienceTotal").isEqualTo(15);
        assertThat(result).extractingJsonPathNumberValue("$.workExperienceCurrent").isEqualTo(5);
        assertThat(result).extractingJsonPathValue("$.employmentPosition")
                .isEqualTo("TOP_MANAGER");
    }

    @Test
    @DisplayName("Десериализация EmploymentDTO.")
    void testDeserialize() throws IOException {
        String jsonString = "{\"employmentStatus\": \"SELF_EMPLOYED\"," +
                " \"employmentINN\": \"123412341234 123\"," +
                " \"salary\": \"50000\"," +
                " \"workExperienceTotal\": 15," +
                " \"workExperienceCurrent\": 5," +
                " \"employmentPosition\": \"TOP_MANAGER\"}";
        EmploymentDTO employmentDTO = jacksonTester.parseObject(jsonString);
        AssertionsForClassTypes.assertThat(employmentDTO.getEmploymentStatus())
                .isEqualTo(EmploymentStatus.SELF_EMPLOYED);
        AssertionsForClassTypes.assertThat(employmentDTO.getEmploymentINN()).isEqualTo("123412341234 123");
        AssertionsForClassTypes.assertThat(employmentDTO.getSalary()).isEqualTo(BigDecimal.valueOf(50000));
        AssertionsForClassTypes.assertThat(employmentDTO.getWorkExperienceTotal()).isEqualTo(15);
        AssertionsForClassTypes.assertThat(employmentDTO.getWorkExperienceCurrent()).isEqualTo(5);
        AssertionsForClassTypes.assertThat(employmentDTO.getEmploymentPosition())
                .isEqualTo(EmploymentPosition.TOP_MANAGER);
    }
}
