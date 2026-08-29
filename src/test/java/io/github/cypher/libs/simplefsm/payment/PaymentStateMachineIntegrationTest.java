package io.github.cypher.libs.simplefsm.payment;

import io.github.cypher.libs.simplefsm.exceptions.BadTransitionException;
import io.github.cypher.libs.simplefsm.exceptions.SimpleFsmException;
import io.github.cypher.libs.simplefsm.models.TransitionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentStateMachineIntegrationTest {
    private InMemoryPaymentRepository repository;
    private PaymentStateMachine stateMachine;
    private Payment payment;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPaymentRepository();
        stateMachine = new PaymentStateMachine(repository);
        payment = repository.save(new Payment("payment-1"));
    }

    @Test
    void completesPaymentEndToEnd() {
        fire(PaymentState.PAYMENT_INTENT, PaymentEvent.INITIATE, PaymentState.PAYMENT_INITIATED);
        fire(PaymentState.PAYMENT_INITIATED, PaymentEvent.PROCESS, PaymentState.PAYMENT_PROCESSING);
        fire(PaymentState.PAYMENT_PROCESSING, PaymentEvent.COMPLETE, PaymentState.PAYMENT_COMPLETED);

        assertPersistedState(PaymentState.PAYMENT_COMPLETED);
    }

    @Test
    void supportsEveryTerminalPaymentPath() {
        fire(PaymentState.PAYMENT_INTENT, PaymentEvent.DISMISS, PaymentState.PAYMENT_INTENT_DISMISSED);
        assertPersistedState(PaymentState.PAYMENT_INTENT_DISMISSED);

        payment = repository.save(new Payment("payment-2"));
        fire(PaymentState.PAYMENT_INTENT, PaymentEvent.INITIATE, PaymentState.PAYMENT_INITIATED);
        fire(PaymentState.PAYMENT_INITIATED, PaymentEvent.ABORT, PaymentState.PAYMENT_ABORTED);
        assertPersistedState(PaymentState.PAYMENT_ABORTED);

        payment = repository.save(new Payment("payment-3"));
        fire(PaymentState.PAYMENT_INTENT, PaymentEvent.INITIATE, PaymentState.PAYMENT_INITIATED);
        fire(PaymentState.PAYMENT_INITIATED, PaymentEvent.PROCESS, PaymentState.PAYMENT_PROCESSING);
        fire(PaymentState.PAYMENT_PROCESSING, PaymentEvent.FAIL, PaymentState.PAYMENT_FAILED);
        assertPersistedState(PaymentState.PAYMENT_FAILED);

        payment = repository.save(new Payment("payment-4"));
        fire(PaymentState.PAYMENT_INTENT, PaymentEvent.INITIATE, PaymentState.PAYMENT_INITIATED);
        fire(PaymentState.PAYMENT_INITIATED, PaymentEvent.PROCESS, PaymentState.PAYMENT_PROCESSING);
        fire(PaymentState.PAYMENT_PROCESSING, PaymentEvent.ABORT, PaymentState.PAYMENT_ABORTED);
        assertPersistedState(PaymentState.PAYMENT_ABORTED);
    }

    @Test
    void rejectsInvalidTransitionWithoutChangingPersistedState() {
        final var context = context(
                PaymentState.PAYMENT_INTENT,
                PaymentEvent.COMPLETE,
                PaymentState.PAYMENT_COMPLETED);

        assertThrows(BadTransitionException.class, () -> stateMachine.fire(context));
        assertPersistedState(PaymentState.PAYMENT_INTENT);
    }

    @Test
    void leavesPersistedStateUnchangedWhenHandlerFails() {
        final var context = new PaymentTransitionContext(
                payment.getId(),
                request(PaymentState.PAYMENT_INTENT,
                        PaymentEvent.INITIATE,
                        PaymentState.PAYMENT_INITIATED),
                true);

        assertThrows(IllegalStateException.class, () -> stateMachine.fire(context));
        assertPersistedState(PaymentState.PAYMENT_INTENT);
    }

    @Test
    void rejectsTransitionKeyThatDoesNotMatchRequest() {
        assertThrows(SimpleFsmException.class, () -> new PaymentTransitionContext(
                payment.getId(),
                request(PaymentState.PAYMENT_INTENT,
                        PaymentEvent.INITIATE,
                        PaymentState.PAYMENT_INITIATED)) {
            @Override
            protected PaymentTransitionKey buildTransitionKey(
                    final TransitionRequest<PaymentState, PaymentEvent> ignored) {
                return new PaymentTransitionKey(PaymentState.PAYMENT_PROCESSING, PaymentEvent.COMPLETE);
            }
        });
    }

    private void fire(final PaymentState from,
                      final PaymentEvent event,
                      final PaymentState target) {
        stateMachine.fire(context(from, event, target));
        assertPersistedState(target);
    }

    private PaymentTransitionContext context(final PaymentState from,
                                             final PaymentEvent event,
                                             final PaymentState target) {
        return new PaymentTransitionContext(payment.getId(), request(from, event, target));
    }

    private static TransitionRequest<PaymentState, PaymentEvent> request(
            final PaymentState from,
            final PaymentEvent event,
            final PaymentState target) {
        return new TransitionRequest<>(from, event, target);
    }

    private void assertPersistedState(final PaymentState expected) {
        assertEquals(expected, repository.findById(payment.getId()).orElseThrow().getState());
    }
}
