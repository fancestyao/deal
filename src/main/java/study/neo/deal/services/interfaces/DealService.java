package study.neo.deal.services.interfaces;

import study.neo.deal.dtos.FinishRegistrationRequestDTO;
import study.neo.deal.dtos.LoanApplicationRequestDTO;
import study.neo.deal.dtos.LoanOfferDTO;

import java.util.List;

public interface DealService {
    List<LoanOfferDTO> application(LoanApplicationRequestDTO loanApplicationRequestDTO);
    void offer(LoanOfferDTO loanOfferDTO);
    void calculate(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId);
}
