package com.example.payservice.controller;

import com.example.payservice.request.PaymentRequest;
import com.example.payservice.service.PaymentService;
import com.siot.IamportRestClient.response.Payment;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pay-service")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{impUid}")
    public Payment info(@PathVariable String impUid) {
        return paymentService.info(impUid);
    }

    @PostMapping("/validate")
    public Payment validate(@RequestBody @Valid PaymentRequest request) {

        return paymentService.valid(request);

    }

    @PostMapping("/cancel")
    public void cancel(@RequestBody @Valid PaymentRequest request) {
        paymentService.cancel(request);
    }
}
