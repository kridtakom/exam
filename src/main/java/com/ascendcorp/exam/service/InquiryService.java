package com.ascendcorp.exam.service;

import com.ascendcorp.exam.model.BANK_RESPONSE_CODE;
import com.ascendcorp.exam.model.InquiryServiceResultDTO;
import com.ascendcorp.exam.model.TransferResponse;
import com.ascendcorp.exam.proxy.BankProxyGateway;
import lombok.NonNull;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.xml.ws.WebServiceException;
import java.util.Date;

@Service
public class InquiryService {

    private final BankProxyGateway bankProxyGateway;

    final static Logger log = Logger.getLogger(InquiryService.class);

    public InquiryService(BankProxyGateway bankProxyGateway) {
        this.bankProxyGateway = bankProxyGateway;
    }

    public InquiryServiceResultDTO inquiry(String transactionId, Date tranDateTime, String channel, String locationCode, String bankCode, String bankNumber, double amount, String reference1, String reference2, String firstName, String lastName) {
        // InquiryServiceResultDTO respDTO = null;
        try {
            log.info("validate request parameters.");
            checkInquiryParams(transactionId, tranDateTime, channel, bankCode, bankNumber, amount);

            log.info("call bank web service");
            TransferResponse response = bankProxyGateway.requestTransfer(transactionId, tranDateTime, channel, bankCode, bankNumber, amount, reference1, reference2);

            log.info("check bank response code");

            if (response != null) {
                return handleBankPoxyGatewayResponse(response);
            } else {
                // no resport from bank
                throw new Exception("Unable to inquiry from service.");
            }
        } catch (NullPointerException ne) {
            // handle error from Inquiry params
            return handleNullPointerException();
        } catch (WebServiceException r) {
            // handle error from bank web service
            String faultString = r.getMessage();
            return handleWebServiceException(faultString);
        } catch (Exception e) {
            // handle exception
            log.error("inquiry exception", e);
            return handleException();
        }
    }

    private void checkInquiryParams(
            @NonNull String transactionId,
            @NonNull Date tranDateTime,
            @NonNull String channel,
            @NonNull String bankCode,
            @NonNull String bankNumber,
            double amount
    ) {
        /*
        if (transactionId == null) {
            log.info("Transaction id is required!");
            throw new NullPointerException("Transaction id is required!");
        }

        if (tranDateTime == null) {
            log.info("Transaction DateTime is required!");
            throw new NullPointerException("Transaction DateTime is required!");
        }
        if (channel == null) {
            log.info("Channel is required!");
            throw new NullPointerException("Channel is required!");
        }
        if (bankCode == null || bankCode.isEmpty()) {
            log.info("Bank Code is required!");
            throw new NullPointerException("Bank Code is required!");
        }
        if (bankNumber == null || bankNumber.isEmpty()) {
            log.info("Bank Number is required!");
            throw new NullPointerException("Bank Number is required!");
        }
         */
        if (bankCode.isEmpty()) {
            log.info("Bank Code is required!");
            throw new NullPointerException("Bank Code is required!");
        }
        if (bankNumber.isEmpty()) {
            log.info("Bank Number is required!");
            throw new NullPointerException("Bank Number is required!");
        }
        if (amount <= 0) {
            log.info("Amount must more than zero!");
            throw new NullPointerException("Amount must more than zero!");
        }
    }

    private InquiryServiceResultDTO handleBankPoxyGatewayResponse(TransferResponse response) throws Exception {
        log.debug("found response code");
        InquiryServiceResultDTO respDTO = new InquiryServiceResultDTO();

        respDTO.setRef_no1(response.getReferenceCode1());
        respDTO.setRef_no2(response.getReferenceCode2());
        respDTO.setAmount(response.getBalance());
        respDTO.setTranID(response.getBankTransactionID());

        if (response.getResponseCode().equalsIgnoreCase(BANK_RESPONSE_CODE.APPROVED.name())) {
            // bank response code = approved
            handleResponseCodeApproved(response, respDTO);
        } else if (response.getResponseCode().equalsIgnoreCase(BANK_RESPONSE_CODE.INVALID_DATA.name())) {
            // bank response code = invalid_data
            handleResponseCodeInvalidDate(response, respDTO);
        } else if (response.getResponseCode().equalsIgnoreCase(BANK_RESPONSE_CODE.TRANSACTION_ERROR.name())) {
            // bank response code = transaction_error
            handleResponseCodeTransactionError(response, respDTO);
        } else if (response.getResponseCode().equalsIgnoreCase(BANK_RESPONSE_CODE.UNKNOWN.name())) {
            // bank response code = unknown
            handleResponseCodeUnknown(response, respDTO);
        } else {
            // bank code not support
            throw new Exception("Unsupport Error Reason Code");
        }
        return respDTO;
    }


    private void handleResponseCodeApproved(TransferResponse response, InquiryServiceResultDTO respDTO) {
        respDTO.setReasonCode("200");
        respDTO.setReasonDesc(response.getDescription());
        respDTO.setAccountName(response.getDescription());
    }

    private void handleResponseCodeInvalidDate(TransferResponse response, InquiryServiceResultDTO respDTO) {
        String replyDesc = response.getDescription();
        respDTO.setReasonCode("400");
        respDTO.setReasonDesc("General Invalid Data");
        if (replyDesc != null) {
            String[] respDesc = replyDesc.split(":");
            if (respDesc.length >= 3) {
                // bank description full format
                respDTO.setReasonCode(respDesc[1]);
                respDTO.setReasonDesc(respDesc[2]);
            }
        }
    }

    private void handleResponseCodeTransactionError(TransferResponse response, InquiryServiceResultDTO respDTO) {
        respDTO.setReasonCode("500");
        respDTO.setReasonDesc("General Transaction Error");
        String replyDesc = response.getDescription();
        if (replyDesc != null) {
            String[] respDesc = replyDesc.split(":");
            if (respDesc.length >= 2) {
                log.info("Case Inquiry Error Code Format Now Will Get From [0] and [1] first");
                String subIdx1 = respDesc[0];
                String subIdx2 = respDesc[1];
                log.info("index[0] : " + subIdx1 + " index[1] is >> " + subIdx2);
                respDTO.setReasonCode(subIdx1);
                respDTO.setReasonDesc(subIdx2);
                if (!"98".equalsIgnoreCase(subIdx1)) {
                    log.info("case error is not 98 code");
                    if (respDesc.length >= 3) {
                        String subIdx3 = respDesc[2];
                        log.info("index[0] : " + subIdx3);
                        respDTO.setReasonCode(subIdx2);
                        respDTO.setReasonDesc(subIdx3);
                    }
                }
            }
        }
    }

    private void handleResponseCodeUnknown(TransferResponse response, InquiryServiceResultDTO respDTO) {
        respDTO.setReasonCode("501");
        respDTO.setReasonDesc("General Invalid Data");
        String replyDesc = response.getDescription();
        if (replyDesc != null) {
            String[] respDesc = replyDesc.split(":");
            if (respDesc.length >= 2) {
                // bank description full format
                respDTO.setReasonCode(respDesc[0]);
                respDTO.setReasonDesc(respDesc[1]);
                if (respDTO.getReasonDesc() == null || respDTO.getReasonDesc().trim().isEmpty()) {
                    respDTO.setReasonDesc("General Invalid Data");
                }
            }
        }
    }

    private InquiryServiceResultDTO handleWebServiceException(String faultString) {
        if (faultString != null && (faultString.contains("java.net.SocketTimeoutException") || faultString.contains("Connection timed out"))) {
            // bank timeout
            return new InquiryServiceResultDTO(null, null, "503", "Error timeout", null, null, null, null, null);
        } else {
            // bank general error
            return handleException();
        }
    }

    private InquiryServiceResultDTO handleNullPointerException() {
        return new InquiryServiceResultDTO(null, null, "500", "General Invalid Data", null, null, null, null, null);
    }

    private InquiryServiceResultDTO handleException() {
        return new InquiryServiceResultDTO(null, null, "504", "Internal Application Error", null, null, null, null, null);
    }
}
