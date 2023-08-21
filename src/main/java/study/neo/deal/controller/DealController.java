package study.neo.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.neo.deal.dto.FinishRegistrationRequestDTO;
import study.neo.deal.dto.LoanApplicationRequestDTO;
import study.neo.deal.dto.LoanOfferDTO;
import study.neo.deal.service.interfaces.DealService;

import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Deal API")
public class DealController {
    private final DealService dealService;

    @PostMapping("/application")
    @Operation(summary = "Рассмотрение заявки на кредит",
            description = "Возвращает лист с 4 LoanOfferDTO по входному LoanApplicationRequestDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция прошла успешно"),
            @ApiResponse(responseCode = "409", description = "Не удалось провалидировать данные"),
    })
    public ResponseEntity<List<LoanOfferDTO>> application(@RequestBody @Parameter(description =
            "Входные параметры для расчета условий кредита для пользователя")
                                                              LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Получен запрос в контроллер на расчет условий кредита с loanApplicationRequestDTO: {}",
                loanApplicationRequestDTO);
        return new ResponseEntity<>(dealService.application(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @PutMapping("/offer")
    @Operation(summary = "Обновление истории статуса заявки на кредит",
            description = "По входному LoanOfferDTO достается соответствующая заявка из БД и обновляется статус," +
                    " история статусов и LoanOfferDTO помещается в поле appliedOffer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция прошла успешно"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена в БД")
    })
    public void offer(@RequestBody @Parameter(description =
            "Входные параметры в виде рассчитанных условий кредита для пользователя") LoanOfferDTO loanOfferDTO) {
        log.info("Получен запрос в контроллер на апдейт рассчитанных условий кредита loanOfferDTO:" +
                        " {} для application с id: {}", loanOfferDTO, loanOfferDTO.getApplicationId());
        dealService.offer(loanOfferDTO);
    }

    @PutMapping("/calculate/{applicationId}")
    @Operation(summary = "Расчет параметров кредита",
            description = "Наполяется ScoringDataDTO по входному FinishRegistrationRequestDTO и " +
                    "уже имеющемуся в БД application. Наполненный ScoringDataDTO отправляется через Post-запрос" +
                    " к МС Конвейер")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция прошла успешно"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена в БД")
    })
    public void calculate(@RequestBody @Parameter(description =
            "Входной параметр наполнения ScoringDataDTO") FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                          @Parameter(description = "По идентификатору ищется соответствующий application")
                          @PathVariable Long applicationId) {
        log.info("Получен запрос в контроллер наполнение ScoringDataDTO с входными данными: {} и applicationId: {}",
                finishRegistrationRequestDTO, applicationId);
        dealService.calculate(finishRegistrationRequestDTO, applicationId);
    }
}