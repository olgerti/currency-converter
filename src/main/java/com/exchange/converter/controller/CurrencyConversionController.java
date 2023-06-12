package com.exchange.converter.controller;


import com.exchange.converter.dto.CurrencyConversionRequestDto;
import com.exchange.converter.dto.CurrencyConversionResponseDto;
import com.exchange.converter.dto.ErrorResponseDto;
import com.exchange.converter.exception.InvalidAmountException;
import com.exchange.converter.exception.InvalidCurrencyException;
import com.exchange.converter.service.CurrencyConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/currency-conversion")
public class CurrencyConversionController {

    @Autowired
    private CurrencyConversionService currencyConversionService;

    @GetMapping
    public ResponseEntity<CurrencyConversionResponseDto> convertCurrency(
            @RequestParam String baseCurrency,
            @RequestParam String targetCurrency,
            @RequestParam BigDecimal amount
    ) {
        CurrencyConversionResponseDto response = new CurrencyConversionResponseDto();
        try {
            CurrencyConversionRequestDto currencyConversionRequestDTO =
                    new CurrencyConversionRequestDto(baseCurrency, targetCurrency, amount);
            response = currencyConversionService.convertCurrency(currencyConversionRequestDTO);
            return ResponseEntity.ok(response);
        } catch (InvalidAmountException | InvalidCurrencyException e) {
            // Exception occurred during validation, return a 400 Bad Request response
            ErrorResponseDto errorResponseDto = new ErrorResponseDto();
            errorResponseDto.setMessage(e.getMessage());
            errorResponseDto.setStatus(400);
            response.setError(errorResponseDto);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // Other unexpected exceptions occurred, return a 500 Internal Server Error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
