package io.github.cypher.libs.simplefsm.payment;

import io.github.cypher.libs.simplefsm.core.BaseTransitionActionHandler;
import io.github.cypher.libs.simplefsm.models.TransitionKey;

import java.util.Set;

public abstract class PaymentTransitionHandler extends BaseTransitionActionHandler<
        PaymentState, PaymentEvent, PaymentTransitionKey, PaymentTransitionContext> {
    private final PaymentRepository repository;

    protected PaymentTransitionHandler(final PaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(final PaymentTransitionContext context) {
        if (context.shouldFailHandler()) {
            throw new IllegalStateException("Simulated payment handler failure");
        }

        final Payment payment = repository.findById(context.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + context.getPaymentId()));
        final var request = context.getTransitionRequest();
        if (payment.getState() != request.fromState()) {
            throw new IllegalStateException("Persisted payment state does not match transition source state");
        }

        payment.transitionTo(request.targetState());
        repository.save(payment);
    }

    protected static Set<TransitionKey<PaymentState, PaymentEvent>> handles(
            final PaymentState fromState,
            final PaymentEvent event) {
        return Set.of(new PaymentTransitionKey(fromState, event));
    }
}

final class InitiatePaymentHandler extends PaymentTransitionHandler {
    InitiatePaymentHandler(final PaymentRepository repository) {
        super(repository);
    }

    @Override
    public Set<TransitionKey<PaymentState, PaymentEvent>> onTransitionKeys() {
        return handles(PaymentState.PAYMENT_INTENT, PaymentEvent.INITIATE);
    }
}

final class DismissPaymentIntentHandler extends PaymentTransitionHandler {
    DismissPaymentIntentHandler(final PaymentRepository repository) {
        super(repository);
    }

    @Override
    public Set<TransitionKey<PaymentState, PaymentEvent>> onTransitionKeys() {
        return handles(PaymentState.PAYMENT_INTENT, PaymentEvent.DISMISS);
    }
}

final class ProcessPaymentHandler extends PaymentTransitionHandler {
    ProcessPaymentHandler(final PaymentRepository repository) {
        super(repository);
    }

    @Override
    public Set<TransitionKey<PaymentState, PaymentEvent>> onTransitionKeys() {
        return handles(PaymentState.PAYMENT_INITIATED, PaymentEvent.PROCESS);
    }
}

final class AbortInitiatedPaymentHandler extends PaymentTransitionHandler {
    AbortInitiatedPaymentHandler(final PaymentRepository repository) {
        super(repository);
    }

    @Override
    public Set<TransitionKey<PaymentState, PaymentEvent>> onTransitionKeys() {
        return handles(PaymentState.PAYMENT_INITIATED, PaymentEvent.ABORT);
    }
}

final class CompletePaymentHandler extends PaymentTransitionHandler {
    CompletePaymentHandler(final PaymentRepository repository) {
        super(repository);
    }

    @Override
    public Set<TransitionKey<PaymentState, PaymentEvent>> onTransitionKeys() {
        return handles(PaymentState.PAYMENT_PROCESSING, PaymentEvent.COMPLETE);
    }
}

final class FailPaymentHandler extends PaymentTransitionHandler {
    FailPaymentHandler(final PaymentRepository repository) {
        super(repository);
    }

    @Override
    public Set<TransitionKey<PaymentState, PaymentEvent>> onTransitionKeys() {
        return handles(PaymentState.PAYMENT_PROCESSING, PaymentEvent.FAIL);
    }
}

final class AbortProcessingPaymentHandler extends PaymentTransitionHandler {
    AbortProcessingPaymentHandler(final PaymentRepository repository) {
        super(repository);
    }

    @Override
    public Set<TransitionKey<PaymentState, PaymentEvent>> onTransitionKeys() {
        return handles(PaymentState.PAYMENT_PROCESSING, PaymentEvent.ABORT);
    }
}
