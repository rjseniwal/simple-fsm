package io.github.cypher.libs.simplefsm.payment;

import io.github.cypher.libs.simplefsm.models.TransitionContext;
import io.github.cypher.libs.simplefsm.models.TransitionRequest;

public class PaymentTransitionContext
        extends TransitionContext<PaymentState, PaymentEvent, PaymentTransitionKey> {
    private final String paymentId;
    private final boolean failHandler;

    public PaymentTransitionContext(final String paymentId,
                                    final TransitionRequest<PaymentState, PaymentEvent> request) {
        this(paymentId, request, false);
    }

    public PaymentTransitionContext(final String paymentId,
                                    final TransitionRequest<PaymentState, PaymentEvent> request,
                                    final boolean failHandler) {
        super(request);
        this.paymentId = paymentId;
        this.failHandler = failHandler;
    }

    @Override
    protected PaymentTransitionKey buildTransitionKey(
            final TransitionRequest<PaymentState, PaymentEvent> request) {
        return new PaymentTransitionKey(request.fromState(), request.event());
    }

    public String getPaymentId() {
        return paymentId;
    }

    public boolean shouldFailHandler() {
        return failHandler;
    }
}
