package study.neo.deal.service.interfaces;

import study.neo.deal.dto.EmailMessage;

public interface KafkaService {
    void sendDocumentsEmail(Long applicationId);
    void signDocumentsEmail(Long applicationId);
    void codeDocumentsEmail(Long applicationId);
    void sendConflictEmail(EmailMessage emailMessage);
}
