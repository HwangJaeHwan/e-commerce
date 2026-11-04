package com.example.payservice.service;

import com.example.payservice.domain.PaymentStatus;
import com.example.payservice.exception.PaymentException;
import com.example.payservice.messagequeue.message.PaymentCancel;
import com.example.payservice.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaymentRefundService {
    private final PaymentRepository payments;
    private final IamportClient client;

    @Transactional
    public void refund(PaymentCancel request) {
        if (request == null || request.getImpUid() == null || request.getImpUid().isBlank()) {
            throw new IllegalArgumentException("환불할 결제 ID는 필수입니다.");
        }
        var detail = payments.findForRefund(request.getImpUid())
                .orElseThrow(() -> new PaymentException("결제 내역이 아직 없으므로 환불을 재시도합니다."));
        if (detail.getPaymentStatus() == PaymentStatus.CANCEL) return;
        try {
            var payment = client.paymentByImpUid(detail.getImpUid()).getResponse();
            if (payment == null) throw new PaymentException("외부 결제 상태를 확인할 수 없습니다.");
            if (!"cancelled".equals(payment.getStatus())) {
                if (!"paid".equals(payment.getStatus())) {
                    throw new PaymentException("환불할 수 없는 결제 상태입니다: " + payment.getStatus());
                }
                // Omitting amount cancels the remaining balance, including after a partial refund.
                var cancelled = client.cancelPaymentByImpUid(new CancelData(detail.getImpUid(), true)).getResponse();
                if (cancelled == null || !"cancelled".equals(cancelled.getStatus())) {
                    throw new PaymentException("외부 결제의 환불 완료를 확인하지 못했습니다.");
                }
            }
            // Reconcile an earlier successful external refund even if its local commit failed.
            detail.markCancelled(request.getReason());
        } catch (IamportResponseException e) {
            throw new PaymentException("결제 환불에 실패했습니다.", e);
        } catch (IOException e) {
            throw new IllegalStateException("결제 환불 통신에 실패했습니다.", e);
        }
    }
}
