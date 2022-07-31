package com.ascendcorp.exam.controller;

import com.ascendcorp.exam.model.InquiryRequestDTO;
import com.ascendcorp.exam.model.InquiryServiceResultDTO;
import com.ascendcorp.exam.service.InquiryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inquiry")
public class InquireController {

    private final InquiryService inquiryService;

    public InquireController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping
    public InquiryServiceResultDTO create(@RequestBody InquiryRequestDTO req) {
        return this.inquiryService.inquiry(
                req.getTransactionId(),
                req.getTranDateTime(),
                req.getChannel(),
                req.getLocationCode(),
                req.getBankCode(),
                req.getBankNumber(),
                req.getAmount(),
                req.getReference1(),
                req.getReference2(),
                req.getFirstName(),
                req.getLastName()
        );
    }

    @GetMapping
    public String helloWorld() {
        return  "Hello World";
    }
}
