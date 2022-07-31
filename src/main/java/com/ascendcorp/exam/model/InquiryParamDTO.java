package com.ascendcorp.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class InquiryParamDTO {
    @NotNull(message = "Transaction id is required!")
    private String transactionId;

    @NotNull(message = "Transaction DateTime is required!")
    private Date tranDateTime;

    @NotNull(message = "Channel is required!")
    private String channel;

    @NotNull(message = "Bank Code is required!")
    @NotEmpty(message = "Bank Code is required!")
    private String bankCode;

    @NotNull(message = "Bank Number is required!")
    @NotEmpty(message = "Bank Number is required!")
    private String bankNumber;

    @Min(value = 1 , message = "Amount must more than zero!")
    private double amount;
}
