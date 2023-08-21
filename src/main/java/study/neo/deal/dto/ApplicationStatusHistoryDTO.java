package study.neo.deal.dto;

import lombok.Builder;
import lombok.Data;
import study.neo.deal.enumeration.ApplicationStatus;
import study.neo.deal.enumeration.ChangeType;

import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationStatusHistoryDTO {
    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}
