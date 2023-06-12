package com.exchange.converter;

import com.exchange.converter.controller.CurrencyConversionController;
import com.exchange.converter.dto.CurrencyConversionRequestDto;
import com.exchange.converter.dto.CurrencyConversionResponseDto;
import com.exchange.converter.dto.ErrorResponseDto;
import com.exchange.converter.exception.InvalidAmountException;
import com.exchange.converter.exception.InvalidCurrencyException;
import com.exchange.converter.service.CurrencyConversionService;
import com.exchange.converter.service.CurrencyValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
class ConverterApplicationTests {

	@InjectMocks
	private CurrencyConversionController currencyConversionController;

	@Mock
	private CurrencyConversionService currencyConversionService;
	@Test
	void contextLoads() {
	}

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CurrencyValidationService currencyValidationService;
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(currencyConversionController).build();
	}

	@Test
	void testConvertCurrency_Success() throws Exception {
		// Arrange
		CurrencyConversionRequestDto request = new CurrencyConversionRequestDto("USD", "EUR", BigDecimal.valueOf(100));
		CurrencyConversionResponseDto response = new CurrencyConversionResponseDto("USD", "EUR", BigDecimal.valueOf(100), BigDecimal.valueOf(90), BigDecimal.valueOf(0.9));
		Mockito.when(currencyConversionService.convertCurrency(request)).thenReturn(response);

		// Act
		MvcResult mvcResult = mockMvc.perform(get("/currency-conversion")
						.param("baseCurrency", "USD")
						.param("targetCurrency", "EUR")
						.param("amount", "100"))
				.andExpect(status().isOk())
				.andReturn();

		// Assert
		String content = mvcResult.getResponse().getContentAsString();

		System.out.println("hello" + content);
		CurrencyConversionResponseDto actualResponse = objectMapper.readValue(content, CurrencyConversionResponseDto.class);
		assertEquals("USD", actualResponse.getBaseCurrency());
		assertEquals("EUR", actualResponse.getTargetCurrency());
		assertEquals(BigDecimal.valueOf(100), actualResponse.getAmount());
		assertEquals(BigDecimal.valueOf(90), actualResponse.getConvertedAmount());
		assertEquals(BigDecimal.valueOf(0.9), actualResponse.getExchangeRate());
	}

	@Test
	void testConvertCurrency_InvalidAmount() throws Exception {
		// Arrange
		Mockito.doThrow(new InvalidAmountException("Invalid amount")).when(currencyValidationService).validateAmount(Mockito.any(BigDecimal.class));

		// Act
		MvcResult mvcResult = mockMvc.perform(get("/currency-conversion")
						.param("baseCurrency", "USD")
						.param("targetCurrency", "EUR")
						.param("amount", "-100"))
				.andExpect(status().isBadRequest())
				.andReturn();

		// Assert
		String content = mvcResult.getResponse().getContentAsString();
		ErrorResponseDto errorResponse = objectMapper.readValue(content, ErrorResponseDto.class);
		assertEquals("Invalid amount", errorResponse.getMessage());
		assertEquals(400, errorResponse.getStatus());
	}

	@Test
	void testConvertCurrency_InvalidCurrency() throws Exception {
		// Arrange
		Mockito.doThrow(new InvalidCurrencyException("Invalid currency")).when(currencyValidationService).validateCurrency(Mockito.anyString());

		// Act
		MvcResult mvcResult = mockMvc.perform(get("/currency-conversion")
						.param("baseCurrency", "XYZ")
						.param("targetCurrency", "XYZ")
						.param("amount", "100"))
				.andExpect(status().isBadRequest())
				.andReturn();

		// Assert
		String content = mvcResult.getResponse().getContentAsString();
		ErrorResponseDto errorResponse = objectMapper.readValue(content, ErrorResponseDto.class);
		assertEquals("Invalid currency", errorResponse.getMessage());
		assertEquals(400, errorResponse.getStatus());
	}

	@Test
	void testConvertCurrency_InternalServerError() throws Exception {
		// Arrange
		Mockito.when(currencyConversionService.convertCurrency(Mockito.any(CurrencyConversionRequestDto.class))).thenThrow(new RuntimeException("Internal server error"));

		// Act
		mockMvc.perform(get("/currency-conversion")
						.param("baseCurrency", "USD")
						.param("targetCurrency", "EUR")
						.param("amount", "100"))
				.andExpect(status().isInternalServerError());
	}


}
