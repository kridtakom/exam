package com.ascendcorp.exam.proxy;

import com.ascendcorp.exam.model.TransferResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Component
public class BankProxyGateway {

    public TransferResponse requestTransfer(
            String transactionId,
            Date tranDateTime,
            String channel,
            String bankCode,
            String bankNumber,
            double amount,
            String reference1,
            String reference2
    ) {

        return new TransferResponse();
    }
}

