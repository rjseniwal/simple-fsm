package io.github.cypher.libs.simplefsm.payment;

import io.github.cypher.libs.simplefsm.models.Event;

public enum PaymentEvent implements Event {
    INITIATE,
    PROCESS,
    COMPLETE,
    ABORT,
    FAIL,
    DISMISS
}
