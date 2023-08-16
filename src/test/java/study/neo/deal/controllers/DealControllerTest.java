package study.neo.deal.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import study.neo.deal.dtos.FinishRegistrationRequestDTO;
import study.neo.deal.dtos.LoanApplicationRequestDTO;
import study.neo.deal.dtos.LoanOfferDTO;
import study.neo.deal.services.interfaces.DealService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест контроллера DealController.")
public class DealControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private DealController dealController;
    @Mock
    private DealService dealService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(dealController).build();
    }

    @Test
    @DisplayName("Тест метода application DealController.")
    public void testApplication() throws Exception {
        LoanApplicationRequestDTO requestDTO = new LoanApplicationRequestDTO();
        List<LoanOfferDTO> responseDTO = new ArrayList<>();
        when(dealService.application(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/deal/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDTO)));

        verify(dealService).application(requestDTO);
    }

    @Test
    @DisplayName("Тест метода offer DealController.")
    public void testOffer() throws Exception {
        LoanOfferDTO requestDTO = new LoanOfferDTO();

        mockMvc.perform(put("/deal/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());

        verify(dealService).offer(requestDTO);
    }

    @Test
    @DisplayName("Тест метода calculate DealController.")
    public void testCalculate() throws Exception {
        Long applicationId = 1L;
        FinishRegistrationRequestDTO requestDTO = new FinishRegistrationRequestDTO();

        mockMvc.perform(put("/deal/calculate/{applicationId}", applicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());

        verify(dealService).calculate(requestDTO, applicationId);
    }
}
