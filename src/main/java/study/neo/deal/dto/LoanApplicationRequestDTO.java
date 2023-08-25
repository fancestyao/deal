package study.neo.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanApplicationRequestDTO {
    @Schema(description = "Желаемая сумма кредита", example = "100000")
    private BigDecimal amount;
    @Schema(description = "Желаемый период погашения кредита (в месяцах)", example = "18")
    private Integer term;
    @Schema(description = "Имя клиента", example = "Ivan")
    private String firstName;
    @Schema(description = "Фамилия клиента", example = "Ivanov")
    private String lastName;
    @Schema(description = "Отчество клиента (при наличии)", example = "Ivanovich")
    private String middleName;
    @Schema(description = "Электронная почта клиента", example = "IvanovII@email.ru")
    private String email;
    @Schema(description = "Дата рождения клиента (в формате yyyy-MM-dd)", example = "1999-05-18")
    private LocalDate birthDate;
    @Schema(description = "Серия паспорта клиента", example = "1234")
    private String passportSeries;
    @Schema(description = "Номер паспорта клиента", example = "123456")
    private String passportNumber;
}