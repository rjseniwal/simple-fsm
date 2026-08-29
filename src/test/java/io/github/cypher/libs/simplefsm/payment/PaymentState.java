package io.github.cypher.libs.simplefsm.payment;

import io.github.cypher.libs.simplefsm.models.State;

public enum PaymentState implements State {
    PAYMENT_INTENT,
    PAYMENT_INITIATED,
    PAYMENT_PROCESSING,
    PAYMENT_COMPLETED,
    PAYMENT_ABORTED,
    PAYMENT_FAILED,
    PAYMENT_INTENT_DISMISSED
}
