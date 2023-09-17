package study.neo.deal.service.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.neo.deal.dto.FinishRegistrationRequestDTO;
import study.neo.deal.dto.LoanApplicationRequestDTO;
import study.neo.deal.dto.LoanOfferDTO;
import study.neo.deal.model.Application;
import study.neo.deal.service.interfaces.*;

import java.util.List;

import static study.neo.deal.enumeration.Theme.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealServiceImpl implements DealService {
    private final ApplicationService applicationService;
    private final OfferService offerService;
    private final CalculateService calculateService;
    private final FeignConveyorClient feignConveyorClient;
    private final KafkaService kafkaService;

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

    @Override
    public void sendDocuments(Long applicationId) {
        log.info("Отправляем emailMessage на MC Dossier (send_documents) с помощью kafkaService");
        kafkaService.sendEmailToDossier(applicationId, SEND_DOCUMENTS);
    }

    @Override
    public void signDocuments(Long applicationId) {
        applicationService.setSesCodeToApplication(applicationId);
        log.info("Отправляем emailMessage на MC Dossier (sens_ses) с помощью kafkaService");
        kafkaService.sendEmailToDossier(applicationId, SEND_SES);
    }

    @Override
    public void codeDocuments(Long applicationId, Integer sesCode) {
        applicationService.validateSesCode(applicationId, sesCode);
    }
}
