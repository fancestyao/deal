package study.neo.deal.services.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.neo.deal.dtos.FinishRegistrationRequestDTO;
import study.neo.deal.dtos.LoanApplicationRequestDTO;
import study.neo.deal.dtos.LoanOfferDTO;
import study.neo.deal.entities.Application;
import study.neo.deal.services.interfaces.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final ApplicationService applicationService;
    private final OfferService offerService;
    private final CalculateService calculateService;
    private final FeignConveyorClient feignConveyorClient;

    @Override
    public List<LoanOfferDTO> application(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Application application = applicationService.configureApplication(loanApplicationRequestDTO);
        List<LoanOfferDTO> result = feignConveyorClient.sendOffers(loanApplicationRequestDTO);
        for (LoanOfferDTO loanOfferDTO : result) {
            loanOfferDTO.setApplicationId(application.getApplicationId());
        }
        return result;
    }

    @Override
    public void offer(LoanOfferDTO loanOfferDTO) {
        offerService.configureOffer(loanOfferDTO);
    }

    @Override
    public void calculate(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        calculateService.configureCalculation(finishRegistrationRequestDTO, applicationId);
    }
}
