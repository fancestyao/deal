package study.neo.deal.service.interfaces;

import study.neo.deal.dto.LoanApplicationRequestDTO;
import study.neo.deal.enumeration.ApplicationStatus;
import study.neo.deal.model.Application;

public interface ApplicationService {
    Application createApplication(LoanApplicationRequestDTO loanApplicationRequestDTO);

    void setSesCodeToApplication(Long applicationId);

    void validateSesCode(Long applicationId, Integer sesCode);

    void updateApplicationStatus(Long applicationId, ApplicationStatus applicationStatus);
}
