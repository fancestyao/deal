package study.neo.deal.service.interfaces;

import feign.FeignException;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import study.neo.deal.dto.CreditDTO;
import study.neo.deal.dto.LoanApplicationRequestDTO;
import study.neo.deal.dto.LoanOfferDTO;
import study.neo.deal.dto.ScoringDataDTO;

import java.util.List;

@FeignClient(name = "conveyor", url = "${feign-client.url}")
public interface FeignConveyorClient {
    @PostMapping("/conveyor/offers")
    List<LoanOfferDTO> getOffers(@RequestBody @Parameter(description =
            "Входные параметры для расчета условий кредита для пользователя")
                                              LoanApplicationRequestDTO loanApplicationRequestDTO)
            throws FeignException.Conflict;
    @PostMapping("/conveyor/calculation")
    CreditDTO getCalculation(@RequestBody @Parameter(description =
            "Входные параметры для расчета параметров кредита")
                         ScoringDataDTO scoringDataDTO);
}
