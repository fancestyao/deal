package study.neo.deal.service.interfaces;

import study.neo.deal.dto.FinishRegistrationRequestDTO;

public interface CalculateService {
    void configureCalculation(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId);
}
