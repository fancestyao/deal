package study.neo.deal.service.interfaces;

import study.neo.deal.dto.FinishRegistrationRequestDTO;
import study.neo.deal.dto.LoanApplicationRequestDTO;
import study.neo.deal.dto.LoanOfferDTO;
import study.neo.deal.model.Application;

import java.util.List;

public interface DealService {
    List<LoanOfferDTO> application(LoanApplicationRequestDTO loanApplicationRequestDTO);

    void offer(LoanOfferDTO loanOfferDTO);

    void calculate(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId);

    void sendDocuments(Long applicationId);

    void signDocuments(Long applicationId);

    void codeDocuments(Long applicationId, Integer sesCode);

    List<Application> getListOfApplications();

    Application getApplicationById(Long applicationId);
}
