package com.example.bank.common.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


@Getter
@Setter
public class PageableDto {
    @Min(1)
    @NotNull
    private int size;
    @Min(1)
    @NotNull
    private int page;
}