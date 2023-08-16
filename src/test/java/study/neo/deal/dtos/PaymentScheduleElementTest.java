package study.neo.deal.dtos;

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
@DisplayName("Тест PaymentScheduleElement.")
public class PaymentScheduleElementTest {
    @Autowired
    private JacksonTester<PaymentScheduleElement> jacksonTester;

    @Test
    @DisplayName("Сериализация PaymentScheduleElement.")
    void testSerialize() throws IOException {
        PaymentScheduleElement paymentScheduleElement = PaymentScheduleElement.builder()
                .number(1)
                .date(LocalDate.of(1999, 5, 18))
                .totalPayment(BigDecimal.valueOf(100000))
                .interestPayment(BigDecimal.valueOf(15000))
                .debtPayment(BigDecimal.valueOf(3500))
                .remainingDebt(BigDecimal.valueOf(81500))
                .build();
        JsonContent<PaymentScheduleElement> result = jacksonTester.write(paymentScheduleElement);
        assertThat(result).extractingJsonPathNumberValue("$.number").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.date").isEqualTo("1999-05-18");
        assertThat(result).extractingJsonPathNumberValue("$.totalPayment").isEqualTo(100000);
        assertThat(result).extractingJsonPathNumberValue("$.interestPayment").isEqualTo(15000);
        assertThat(result).extractingJsonPathNumberValue("$.debtPayment").isEqualTo(3500);
        assertThat(result).extractingJsonPathNumberValue("$.remainingDebt").isEqualTo(81500);
    }

    @Test
    @DisplayName("Десериализация PaymentScheduleElement.")
    void testDeserialize() throws IOException {
        String jsonString = "{\"number\": \"1\"," +
                " \"date\": \"1999-05-18\"," +
                " \"totalPayment\": \"100000\"," +
                " \"interestPayment\": 15000," +
                " \"debtPayment\": 3500," +
                " \"remainingDebt\": \"81500\"}";
        PaymentScheduleElement paymentScheduleElement = jacksonTester.parseObject(jsonString);
        AssertionsForClassTypes.assertThat(paymentScheduleElement.getNumber()).isEqualTo(1);
        AssertionsForClassTypes.assertThat(paymentScheduleElement.getDate()).isEqualTo(LocalDate.of(1999, 5, 18));
        AssertionsForClassTypes.assertThat(paymentScheduleElement.getTotalPayment())
                .isEqualTo(BigDecimal.valueOf(100000));
        AssertionsForClassTypes.assertThat(paymentScheduleElement.getInterestPayment())
                .isEqualTo(BigDecimal.valueOf(15000));
        AssertionsForClassTypes.assertThat(paymentScheduleElement.getDebtPayment())
                .isEqualTo(BigDecimal.valueOf(3500));
        AssertionsForClassTypes.assertThat(paymentScheduleElement.getRemainingDebt())
                .isEqualTo(BigDecimal.valueOf(81500));
    }
}
