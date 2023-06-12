package com.exchange.converter.mapper;


import com.exchange.converter.dto.CurrencyConversionRequestDto;
import com.exchange.converter.dto.CurrencyConversionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    CurrencyConversionResponseDto convertToResponseDto(CurrencyConversionRequestDto currencyConversionRequestDto);
}
