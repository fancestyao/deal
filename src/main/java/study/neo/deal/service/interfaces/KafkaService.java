package study.neo.deal.service.interfaces;

import study.neo.deal.enumeration.Theme;

public interface KafkaService {
    void sendEmailToDossier(Long applicationId, Theme theme, String themeName);
}
