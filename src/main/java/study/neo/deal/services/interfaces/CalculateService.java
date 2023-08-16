package study.neo.deal.services.interfaces;

import study.neo.deal.dtos.FinishRegistrationRequestDTO;

public interface CalculateService {
    void configureCalculation(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId);
}
