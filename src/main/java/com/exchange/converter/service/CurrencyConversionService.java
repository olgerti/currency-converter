package com.exchange.converter.service;

import com.exchange.converter.cache.ExchangeRateCache;
import com.exchange.converter.dto.ConversionRateResponseDto;
import com.exchange.converter.dto.CurrencyConversionRequestDto;
import com.exchange.converter.dto.CurrencyConversionResponseDto;
import com.exchange.converter.exception.InvalidAmountException;
import com.exchange.converter.exception.InvalidCurrencyException;
import com.exchange.converter.mapper.CurrencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyConversionService {
    private final ExternalApiService externalApiService;
    private final ExchangeRateCache exchangeRateCache;


    @Autowired
    private CurrencyValidationService currencyValidationService;

    public CurrencyConversionService(ExternalApiService externalApiService,
                                     ExchangeRateCache exchangeRateCache) {
        this.externalApiService = externalApiService;
        this.exchangeRateCache = exchangeRateCache;
    }

    public BigDecimal getExchangeRate(CurrencyConversionRequestDto currencyConversionRequestDTO) {
        String cacheKey = currencyConversionRequestDTO.getBaseCurrency() + "_"
                + currencyConversionRequestDTO.getTargetCurrency();
        BigDecimal exchangeRate = exchangeRateCache.getExchangeRate(cacheKey);
        if (exchangeRate != null) {
            return exchangeRate;
        }

        ConversionRateResponseDto conversionRateResponseDto =
                externalApiService.getExchangeRate(currencyConversionRequestDTO);

        exchangeRate = conversionRateResponseDto.getQuotes()
                .get(currencyConversionRequestDTO.getBaseCurrency() + currencyConversionRequestDTO.getTargetCurrency());

        exchangeRateCache.putExchangeRate(cacheKey, exchangeRate);

        return exchangeRate;
    }

    public CurrencyConversionResponseDto convertCurrency(CurrencyConversionRequestDto currencyConversionRequestDTO)
            throws InvalidAmountException, InvalidCurrencyException {

        try {
            //Validate inputs
            currencyValidationService.validateCurrency(currencyConversionRequestDTO.getBaseCurrency());
            currencyValidationService.validateCurrency(currencyConversionRequestDTO.getTargetCurrency());
            currencyValidationService.validateAmount(currencyConversionRequestDTO.getAmount());

            // All necessary validations and checks have been successfully passed at this point,
            // init convention
            BigDecimal exchangeRate = getExchangeRate(currencyConversionRequestDTO);
            BigDecimal convertedAmount = currencyConversionRequestDTO.getAmount().multiply(exchangeRate);
            CurrencyConversionResponseDto response = CurrencyMapper.INSTANCE.convertToResponseDto(currencyConversionRequestDTO);
            response.setConvertedAmount(convertedAmount);
            response.setExchangeRate(exchangeRate);
            return response;
        } catch (InvalidAmountException iae) {
            throw new InvalidAmountException("Invalid amount: " + currencyConversionRequestDTO.getAmount());
        } catch (InvalidCurrencyException ice) {
            throw new InvalidCurrencyException(ice.getLocalizedMessage());
        }
    }
}

