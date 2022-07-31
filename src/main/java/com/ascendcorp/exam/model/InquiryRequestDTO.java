package com.ascendcorp.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquiryRequestDTO {
    private String transactionId;
    private Date tranDateTime;
    private String channel;
    private String locationCode;
    private String bankCode;
    private String bankNumber;
    private double amount;
    private String reference1;
    private String reference2;
    private String firstName;
    private String lastName;
}
