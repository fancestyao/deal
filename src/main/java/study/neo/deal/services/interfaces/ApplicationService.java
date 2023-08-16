package study.neo.deal.services.interfaces;

import study.neo.deal.dtos.LoanApplicationRequestDTO;
import study.neo.deal.entities.Application;

public interface ApplicationService {
    Application configureApplication(LoanApplicationRequestDTO loanApplicationRequestDTO);
}
