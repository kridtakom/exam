package com.ascendcorp.exam.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TransferResponse {


    private String responseCode;

    private String description;

    @Setter(AccessLevel.NONE)
    private String referenceCode1;

    @Setter(AccessLevel.NONE)
    private String referenceCode2;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String amount;

    @Setter(AccessLevel.NONE)
    private String bankTransactionID;

    public String getBalance() {
        return amount;
    }
}
