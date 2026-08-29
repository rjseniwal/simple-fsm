package io.github.cypher.libs.simplefsm.payment;

import java.util.Objects;

public final class Payment {
    private final String id;
    private PaymentState state;

    public Payment(final String id) {
        this.id = Objects.requireNonNull(id);
        this.state = PaymentState.PAYMENT_INTENT;
    }

    public String getId() {
        return id;
    }

    public PaymentState getState() {
        return state;
    }

    public void transitionTo(final PaymentState state) {
        this.state = Objects.requireNonNull(state);
    }
}
