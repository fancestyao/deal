package study.neo.deal.dto;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import study.neo.deal.enumeration.ApplicationStatus;
import study.neo.deal.enumeration.ChangeType;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@DisplayName("Тест ApplicationStatusHistory.")
public class ApplicationStatusHistoryDTOTest {
    @Autowired
    private JacksonTester<ApplicationStatusHistoryDTO> jacksonTester;

    @Test
    @DisplayName("Сериализация ApplicationStatusHistory.")
    void testSerialize() throws IOException {
        ApplicationStatusHistoryDTO applicationStatusHistoryDTO = ApplicationStatusHistoryDTO.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .changeType(ChangeType.MANUAL)
                .time(LocalDateTime.of(1999, 3, 13, 1, 20, 0))
                .build();
        JsonContent<ApplicationStatusHistoryDTO> result = jacksonTester.write(applicationStatusHistoryDTO);
        assertThat(result).extractingJsonPathValue("$.status")
                .isEqualTo("PREAPPROVAL");
        assertThat(result).extractingJsonPathValue("$.changeType")
                .isEqualTo("MANUAL");
        assertThat(result).extractingJsonPathStringValue("$.time")
                .isEqualTo("1999-03-13T01:20:00");
    }

    @Test
    @DisplayName("Десериализация ApplicationStatusHistory.")
    void testDeserialize() throws IOException {
        String jsonString = "{\"status\": \"PREAPPROVAL\"," +
                " \"changeType\": \"MANUAL\"," +
                " \"time\": \"1999-03-13T01:20:00\"}";
        ApplicationStatusHistoryDTO applicationStatusHistoryDTO = jacksonTester.parseObject(jsonString);
        AssertionsForClassTypes.assertThat(applicationStatusHistoryDTO.getStatus())
                .isEqualTo(ApplicationStatus.PREAPPROVAL);
        AssertionsForClassTypes.assertThat(applicationStatusHistoryDTO.getChangeType())
                .isEqualTo(ChangeType.MANUAL);
        AssertionsForClassTypes.assertThat(applicationStatusHistoryDTO.getTime())
                .isEqualTo("1999-03-13T01:20:00");
    }
}
