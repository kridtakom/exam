package com.ascendcorp.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquiryServiceResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tranID;

    private String namespace;

    private String reasonCode;

    private String reasonDesc;

    private String balance;

    private String ref_no1;

    private String ref_no2;

    private String amount;

    private String accountName = null;


}
