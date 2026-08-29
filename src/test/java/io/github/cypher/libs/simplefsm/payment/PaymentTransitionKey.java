package io.github.cypher.libs.simplefsm.payment;

import io.github.cypher.libs.simplefsm.models.TransitionKey;

public final class PaymentTransitionKey extends TransitionKey<PaymentState, PaymentEvent> {
    public PaymentTransitionKey(final PaymentState fromState, final PaymentEvent event) {
        super(fromState, event);
    }
}
