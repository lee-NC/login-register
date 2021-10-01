package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.Payment;
import com.jasu.loginregister.Repository.PaymentRepository;
import com.jasu.loginregister.Service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public void createPayment(Payment payment) {
        try {
            paymentRepository.saveAndFlush(payment);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
