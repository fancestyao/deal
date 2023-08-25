package study.neo.deal.service.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.neo.deal.dto.FinishRegistrationRequestDTO;
import study.neo.deal.dto.LoanApplicationRequestDTO;
import study.neo.deal.dto.LoanOfferDTO;
import study.neo.deal.model.Application;
import study.neo.deal.service.interfaces.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final ApplicationService applicationService;
    private final OfferService offerService;
    private final CalculateService calculateService;
    private final FeignConveyorClient feignConveyorClient;

    @Transactional
    @Override
    public List<LoanOfferDTO> application(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Application application = applicationService.createApplication(loanApplicationRequestDTO);
        List<LoanOfferDTO> result = feignConveyorClient.getOffers(loanApplicationRequestDTO);
        for (LoanOfferDTO loanOfferDTO : result) {
            loanOfferDTO.setApplicationId(application.getApplicationId());
        }
        return result;
    }

    @Transactional
    @Override
    public void offer(LoanOfferDTO loanOfferDTO) {
        offerService.configureOffer(loanOfferDTO);
    }

    @Override
    public void calculate(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {
        calculateService.configureCalculation(finishRegistrationRequestDTO, applicationId);
    }
}
