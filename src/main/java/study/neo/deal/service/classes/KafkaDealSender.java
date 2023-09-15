package study.neo.deal.service.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import study.neo.deal.dto.EmailMessage;

@Component
@RequiredArgsConstructor
public class KafkaDealSender {
    private final KafkaTemplate<String, EmailMessage> simpleKafkaTemplate;

    public void sendMessage(EmailMessage message, String topicName) {
        simpleKafkaTemplate.send(topicName, message);
    }
}
