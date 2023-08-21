package study.neo.deal.dto;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@DisplayName("Тест LoanApplicationRequestDTO.")
public class LoanApplicationRequestDTOTest {
    @Autowired
    private JacksonTester<LoanApplicationRequestDTO> jacksonTester;

    @Test
    @DisplayName("Десериализация LoanApplicationRequestDTO.")
    void testSerialize() throws IOException {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .middleName("TestMiddleName")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        JsonContent<LoanApplicationRequestDTO> result = jacksonTester.write(loanApplicationRequestDTO);
        assertThat(result).extractingJsonPathStringValue("$.firstName").isEqualTo("TestFirstName");
        assertThat(result).extractingJsonPathStringValue("$.lastName").isEqualTo("TestLastName");
        assertThat(result).extractingJsonPathStringValue("$.middleName").isEqualTo("TestMiddleName");
        assertThat(result).extractingJsonPathNumberValue("$.term").isEqualTo(6);
        assertThat(result).extractingJsonPathNumberValue("$.amount").isEqualTo(13000);
        assertThat(result).extractingJsonPathValue("$.birthDate").isEqualTo("1999-05-18");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("TestEmail@mail.ru");
        assertThat(result).extractingJsonPathStringValue("$.passportSeries").isEqualTo("1234");
        assertThat(result).extractingJsonPathStringValue("$.passportNumber").isEqualTo("123456");
    }

    @Test
    @DisplayName("Десериализация LoanApplicationRequestDTO.")
    void testDeserialize() throws IOException {
        String jsonString = "{\"firstName\": \"TestFirstName\"," +
                " \"lastName\": \"TestLastName\"," +
                " \"middleName\": \"TestMiddleName\"," +
                " \"term\": 6," +
                " \"amount\": 13000," +
                " \"birthDate\": \"1999-05-18\"," +
                " \"email\": \"TestEmail@mail.ru\"," +
                " \"passportSeries\": \"1234\"," +
                " \"passportNumber\": \"123456\"}";
        LoanApplicationRequestDTO loanApplicationRequestDTO = jacksonTester.parseObject(jsonString);
        AssertionsForClassTypes.assertThat(loanApplicationRequestDTO.getFirstName()).isEqualTo("TestFirstName");
        AssertionsForClassTypes.assertThat(loanApplicationRequestDTO.getLastName()).isEqualTo("TestLastName");
        AssertionsForClassTypes.assertThat(loanApplicationRequestDTO.getMiddleName()).isEqualTo("TestMiddleName");
        AssertionsForClassTypes.assertThat(loanApplicationRequestDTO.getTerm()).isEqualTo(6);
        AssertionsForClassTypes.assertThat(loanApplicationRequestDTO.getAmount()).isEqualTo(BigDecimal.valueOf(13000));
        AssertionsForClassTypes.assertThat(loanApplicationRequestDTO.getBirthDate()).isEqualTo("1999-05-18");
        AssertionsForClassTypes.assertThat(loanApplicationRequestDTO.getEmail()).isEqualTo("TestEmail@mail.ru");
        AssertionsForClassTypes.assertThat(loanApplicationRequestDTO.getPassportSeries()).isEqualTo("1234");
        AssertionsForClassTypes.assertThat(loanApplicationRequestDTO.getPassportNumber()).isEqualTo("123456");
    }
}
