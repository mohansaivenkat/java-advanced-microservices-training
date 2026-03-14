package com.rechargex.rechargeservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RechargeRequestDTO {

    @NotBlank
    @Pattern(regexp="[0-9]{10}")
    private String mobileNumber;

    @NotNull
    @Min(1)
    private Long operatorId;

    @NotNull
    @Min(1)
    private Long planId;

    @NotBlank
    private String paymentMethod;
}
