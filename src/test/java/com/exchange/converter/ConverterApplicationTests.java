package com.exchange.converter;

import com.exchange.converter.dto.CurrencyConversionRequestDto;
import com.exchange.converter.dto.CurrencyConversionResponseDto;
import com.exchange.converter.service.CurrencyConversionService;
import com.exchange.converter.service.CurrencyValidationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ConverterApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CurrencyValidationService currencyValidationService;

    @Autowired
    private CurrencyConversionService currencyConversionService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void testConvertCurrency_Success() throws Exception {

        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal exchangeRate = BigDecimal.valueOf(90);

        CurrencyConversionRequestDto currencyConversionRequestDto
                = new CurrencyConversionRequestDto("USD", "ALL", exchangeRate);

        Mockito.when(currencyConversionService.getExchangeRate(currencyConversionRequestDto))
                .thenReturn(BigDecimal.valueOf(90));

        //fetch quotes with same tag
        MvcResult mvcResult = mockMvc.perform(get("/currency-conversion")
                        .queryParam("baseCurrency", "USD")
                        .queryParam("targetCurrency", "EUR")
                        .queryParam("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        CurrencyConversionResponseDto currencyConversionResponseDto = mapper.readValue(response, new TypeReference<>() {
        });

        assertEquals("USD", currencyConversionResponseDto.getBaseCurrency());
        assertEquals("EUR", currencyConversionResponseDto.getTargetCurrency());
        assertEquals(BigDecimal.valueOf(100), currencyConversionResponseDto.getAmount());
        assertEquals(amount.multiply(exchangeRate), currencyConversionResponseDto.getConvertedAmount());
    }

    @Test
    void testConvertCurrency_InvalidAmount() throws Exception {

        String amount = "-100";

        MvcResult mvcResult = mockMvc.perform(get("/currency-conversion")
                        .param("baseCurrency", "USD")
                        .param("targetCurrency", "EUR")
                        .param("amount", amount))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        CurrencyConversionResponseDto currencyConversionResponseDto = mapper.readValue(response, new TypeReference<>() {
        });
        System.out.println(currencyConversionResponseDto.getError().getMessage());
        assertEquals("Invalid amount: " + amount, currencyConversionResponseDto.getError().getMessage());
        assertEquals(400, currencyConversionResponseDto.getError().getStatus());

    }

    @Test
    void testConvertCurrency_InvalidCurrency() throws Exception {
        // Act
        MvcResult mvcResult = mockMvc.perform(get("/currency-conversion")
                        .param("baseCurrency", "XYZ")
                        .param("targetCurrency", "EUR")
                        .param("amount", "100"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        CurrencyConversionResponseDto currencyConversionResponseDto = mapper.readValue(response, new TypeReference<>() {
        });
        System.out.println(currencyConversionResponseDto.getError().getMessage());
        assertEquals("Invalid currency provided", currencyConversionResponseDto.getError().getMessage());
        assertEquals(400, currencyConversionResponseDto.getError().getStatus());
    }

}
