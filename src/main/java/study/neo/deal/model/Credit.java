package study.neo.deal.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import study.neo.deal.dto.PaymentScheduleElement;
import study.neo.deal.enumeration.CreditStatus;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "credit")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_id")
    private Long creditId;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "term")
    private Integer term;
    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;
    @Column(name = "rate")
    private BigDecimal rate;
    @Column(name = "psk")
    private BigDecimal psk;
    @Type(JsonType.class)
    @Column(name = "payment_schedule", columnDefinition = "jsonb")
    private List<PaymentScheduleElement> paymentSchedule;
    @Column(name = "insurance_enable", nullable = false)
    private Boolean insuranceEnable;
    @Column(name = "salary_client", nullable = false)
    private Boolean salaryClient;
    @Column(name = "credit_status")
    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;
}
