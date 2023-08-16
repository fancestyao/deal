package study.neo.deal.dtos;

import lombok.Builder;
import lombok.Data;
import study.neo.deal.enums.ApplicationStatus;
import study.neo.deal.enums.ChangeType;

import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationStatusHistoryDTO {
    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}
