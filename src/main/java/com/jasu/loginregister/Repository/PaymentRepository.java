package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Payment findByToken(String code);

    Payment findTopByCreatedBy(String userId);
}
