package com.example.springboot.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;


public record ProductRecordDTO(@NotBlank String name, @NotNull BigDecimal value ) {
    
}
