package com.example.payservice.service;

import com.example.payservice.client.ItemServiceClient;
import com.example.payservice.exception.PaymentException;
import com.example.payservice.request.ItemQuantity;
import com.example.payservice.request.PaymentRequest;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final IamportClient iamportClient;
    private final ItemServiceClient itemService;


    public Payment info(String impUid) {
        try {

            return iamportClient.paymentByImpUid(impUid).getResponse();

        } catch (IamportResponseException e) {
            throw new PaymentException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public Payment valid(PaymentRequest request) {
        
        try {
            Payment payment = iamportClient.paymentByImpUid(request.getImpUid()).getResponse();

            if (itemService.amount(request.getItems()).compareTo(payment.getAmount()) != 0) {

                iamportClient.cancelPaymentByImpUid(new CancelData(request.getImpUid(), true));

            }

            return payment;



        } catch (IamportResponseException e) {
            throw new PaymentException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Payment cancel(PaymentRequest request) {

        try {

            iamportClient.cancelPaymentByImpUid(new CancelData(request.getImpUid(), true,
                    itemService.amount(request.getItems())));

            return iamportClient.paymentByImpUid(request.getImpUid()).getResponse();

        } catch (IamportResponseException e) {
            throw new PaymentException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
