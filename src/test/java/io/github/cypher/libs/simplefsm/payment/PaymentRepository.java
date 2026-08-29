package io.github.cypher.libs.simplefsm.payment;

import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);

    Optional<Payment> findById(String paymentId);
}
