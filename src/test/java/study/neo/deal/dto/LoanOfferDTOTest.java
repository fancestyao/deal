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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@DisplayName("Тест LoanOfferDTO.")
public class LoanOfferDTOTest {
    @Autowired
    private JacksonTester<LoanOfferDTO> jacksonTester;

    @Test
    @DisplayName("Сериализация LoanOfferDTO.")
    void testSerialize() throws IOException {
        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder()
                .applicationId(1L)
                .requestedAmount(BigDecimal.valueOf(100000))
                .term(12)
                .isInsuranceEnabled(Boolean.TRUE)
                .isSalaryClient(Boolean.TRUE)
                .totalAmount(BigDecimal.valueOf(120000))
                .monthlyPayment(BigDecimal.valueOf(12000))
                .rate(BigDecimal.valueOf(8.5))
                .build();
        JsonContent<LoanOfferDTO> result = jacksonTester.write(loanOfferDTO);
        assertThat(result).extractingJsonPathNumberValue("$.applicationId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.requestedAmount").isEqualTo(100000);
        assertThat(result).extractingJsonPathNumberValue("$.term").isEqualTo(12);
        assertThat(result).extractingJsonPathValue("$.isInsuranceEnabled").isEqualTo(Boolean.TRUE);
        assertThat(result).extractingJsonPathValue("$.isSalaryClient").isEqualTo(Boolean.TRUE);
        assertThat(result).extractingJsonPathNumberValue("$.totalAmount").isEqualTo(120000);
        assertThat(result).extractingJsonPathNumberValue("$.monthlyPayment").isEqualTo(12000);
        assertThat(result).extractingJsonPathNumberValue("$.rate").isEqualTo(8.5);
    }

    @Test
    @DisplayName("Десериализация LoanOfferDTO.")
    void testDeserialize() throws IOException {
        String jsonString = "{\"applicationId\": \"1\"," +
                " \"requestedAmount\": \"100000\"," +
                " \"term\": \"12\"," +
                " \"isInsuranceEnabled\": true," +
                " \"isSalaryClient\": true," +
                " \"monthlyPayment\": \"120000\"," +
                " \"rate\": \"8.5\"}";
        LoanOfferDTO loanOfferDTO = jacksonTester.parseObject(jsonString);
        AssertionsForClassTypes.assertThat(loanOfferDTO.getApplicationId()).isEqualTo(1L);
        AssertionsForClassTypes.assertThat(loanOfferDTO.getRequestedAmount()).isEqualTo(BigDecimal.valueOf(100000));
        AssertionsForClassTypes.assertThat(loanOfferDTO.getTerm()).isEqualTo(12);
        AssertionsForClassTypes.assertThat(loanOfferDTO.getIsInsuranceEnabled()).isEqualTo(true);
        AssertionsForClassTypes.assertThat(loanOfferDTO.getIsSalaryClient()).isEqualTo(true);
        AssertionsForClassTypes.assertThat(loanOfferDTO.getMonthlyPayment()).isEqualTo(BigDecimal.valueOf(120000));
        AssertionsForClassTypes.assertThat(loanOfferDTO.getRate()).isEqualTo(BigDecimal.valueOf(8.5));
    }
}