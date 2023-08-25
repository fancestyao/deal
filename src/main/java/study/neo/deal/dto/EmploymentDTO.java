package study.neo.deal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.neo.deal.enumeration.EmploymentStatus;
import study.neo.deal.enumeration.EmploymentPosition;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmploymentDTO {
    private EmploymentStatus employmentStatus;
    private String employmentINN;
    private BigDecimal salary;
    private EmploymentPosition employmentPosition;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}