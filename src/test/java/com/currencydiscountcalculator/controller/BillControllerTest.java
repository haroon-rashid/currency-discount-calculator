package com.currencydiscountcalculator.controller;

import com.currencydiscountcalculator.service.BillService;
import com.currencydiscountcalculator.dto.BillRequestDTO;
import com.currencydiscountcalculator.dto.BillResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BillControllerTest {

    @Mock
    private BillService billService;

    @InjectMocks
    private BillController billController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(billController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCalculateBill_Success() throws Exception {
        BillRequestDTO billRequestDTO = new BillRequestDTO();
        BillResponseDTO billResponseDTO = new BillResponseDTO();
        billResponseDTO.setTotalPayableAmount(new BigDecimal("100.00"));
        billResponseDTO.setPercentageDiscount(new BigDecimal("5.00"));
        billResponseDTO.setTotalDiscount(new BigDecimal("10.00"));
        billResponseDTO.setDiscountsOnEveryHundredUSD(new BigDecimal("5.00"));
        when(billService.calculateConvertedAmount(billRequestDTO)).thenReturn(ResponseEntity.ok(billResponseDTO));
        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(billRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPayableAmount").value("100.0"))
                .andExpect(jsonPath("$.percentageDiscount").value("5.0"))
                .andExpect(jsonPath("$.totalDiscount").value("10.0"))
                .andExpect(jsonPath("$.discountsOnEveryHundredUSD").value("5.0"));
        verify(billService, times(1)).calculateConvertedAmount(billRequestDTO);
    }

    @Test
    void testCalculateBill_InternalServerError() throws Exception {

        BillRequestDTO billRequestDTO = new BillRequestDTO();
        when(billService.calculateConvertedAmount(billRequestDTO))
                .thenThrow(new RuntimeException("Internal server error"));
        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(billRequestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));

        // Verify interactions
        verify(billService, times(1)).calculateConvertedAmount(billRequestDTO);
    }
}

