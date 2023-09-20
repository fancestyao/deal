package study.neo.deal.dto;

import lombok.Builder;
import lombok.Data;
import study.neo.deal.enumeration.Theme;

@Data
@Builder
public class EmailMessage {
    private String address;
    private Theme theme;
    private Long applicationId;
    private Integer sesCode;
}
