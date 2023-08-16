package study.neo.deal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanOfferDTO {
    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long applicationId;
    @Schema(description = "Желаемая сумма кредита", example = "100000")
    private BigDecimal requestedAmount;
    @Schema(description = "Желаемый период погашения кредита (в месяцах)", example = "18")
    private Integer term;
    @Schema(description = "Имеет ли клиент страхование", example = "true")
    private Boolean isInsuranceEnabled;
    @Schema(description = "Имеет ли клиент заработную плату", example = "false")
    private Boolean isSalaryClient;
    @Schema(description = "Конечная сумма кредита", example = "150000")
    private BigDecimal totalAmount;
    @Schema(description = "Месячная выплата по кредиту", example = "15000")
    private BigDecimal monthlyPayment;
    @Schema(description = "Кредитная ставка", example = "17")
    private BigDecimal rate;
}