package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.Payment;

public interface PaymentService {
    void createPayment(Payment payment);

    Payment findByToken(String code);

    void updatePayment(Payment payment);

    Payment findByCreatedBy(String userId);
}
