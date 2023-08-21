package study.neo.deal.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import study.neo.deal.dto.EmploymentDTO;
import study.neo.deal.enumeration.Gender;
import study.neo.deal.enumeration.MaritalStatus;
import study.neo.deal.dto.Passport;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "email")
    private String email;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "marital_status")
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    @Column(name = "dependent_amount")
    private Integer dependentAmount;
    @Column(name = "passport", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Passport passport;
    @Column(name = "employment", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private EmploymentDTO employment;
    @Column(name = "account")
    private String account;
}
