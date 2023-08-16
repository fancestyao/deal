package study.neo.deal.services.interfaces;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import study.neo.deal.dtos.LoanApplicationRequestDTO;
import study.neo.deal.dtos.LoanOfferDTO;
import study.neo.deal.dtos.ScoringDataDTO;

import java.util.List;

@FeignClient(name = "conveyor", url = "http://localhost:8080")
public interface FeignConveyorClient {
    @PostMapping("/conveyor/offers")
    List<LoanOfferDTO> sendOffers(@RequestBody @Parameter(description =
            "Входные параметры для расчета условий кредита для пользователя")
                                              LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping("/conveyor/calculation")
    void sendCalculation(@RequestBody @Parameter(description =
            "Входные параметры для расчета параметров кредита")
                         ScoringDataDTO scoringDataDTO);
}
