package io.github.cypher.libs.simplefsm.payment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class InMemoryPaymentRepository implements PaymentRepository {
    private final Map<String, Payment> payments = new HashMap<>();

    @Override
    public Payment save(final Payment payment) {
        payments.put(payment.getId(), payment);
        return payment;
    }

    @Override
    public Optional<Payment> findById(final String paymentId) {
        return Optional.ofNullable(payments.get(paymentId));
    }
}
