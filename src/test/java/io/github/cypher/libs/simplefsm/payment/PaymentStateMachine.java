package io.github.cypher.libs.simplefsm.payment;

import io.github.cypher.libs.simplefsm.actions.Action;
import io.github.cypher.libs.simplefsm.core.BaseStateMachine;
import io.github.cypher.libs.simplefsm.core.BaseTransitionActionHandler;
import io.github.cypher.libs.simplefsm.core.StateMachineBuilder;
import io.github.cypher.libs.simplefsm.core.StateMachineDefinition;
import io.github.cypher.libs.simplefsm.core.TransitionActionHandlerProvider;
import io.github.cypher.libs.simplefsm.models.Transition;
import io.github.cypher.libs.simplefsm.models.TransitionKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PaymentStateMachine extends BaseStateMachine<
        PaymentState, PaymentEvent, PaymentTransitionKey, PaymentTransitionContext> {

    public PaymentStateMachine(final PaymentRepository repository) {
        super(definition(), provider(repository), (context, error) -> {
        }, Action.noOpEventAction(), Action.noOpEventAction());
    }

    private static StateMachineDefinition<PaymentState, PaymentEvent> definition() {
        return new StateMachineBuilder<PaymentState, PaymentEvent>(
                PaymentState.PAYMENT_INTENT,
                Set.of(
                        PaymentState.PAYMENT_COMPLETED,
                        PaymentState.PAYMENT_ABORTED,
                        PaymentState.PAYMENT_FAILED,
                        PaymentState.PAYMENT_INTENT_DISMISSED))
                .addTransition(transition(PaymentState.PAYMENT_INTENT,
                        PaymentState.PAYMENT_INITIATED, PaymentEvent.INITIATE))
                .addTransition(transition(PaymentState.PAYMENT_INTENT,
                        PaymentState.PAYMENT_INTENT_DISMISSED, PaymentEvent.DISMISS))
                .addTransition(transition(PaymentState.PAYMENT_INITIATED,
                        PaymentState.PAYMENT_PROCESSING, PaymentEvent.PROCESS))
                .addTransition(transition(PaymentState.PAYMENT_INITIATED,
                        PaymentState.PAYMENT_ABORTED, PaymentEvent.ABORT))
                .addTransition(transition(PaymentState.PAYMENT_PROCESSING,
                        PaymentState.PAYMENT_COMPLETED, PaymentEvent.COMPLETE))
                .addTransition(transition(PaymentState.PAYMENT_PROCESSING,
                        PaymentState.PAYMENT_FAILED, PaymentEvent.FAIL))
                .addTransition(transition(PaymentState.PAYMENT_PROCESSING,
                        PaymentState.PAYMENT_ABORTED, PaymentEvent.ABORT))
                .build();
    }

    private static TransitionActionHandlerProvider<PaymentState, PaymentEvent,
            PaymentTransitionKey, PaymentTransitionContext> provider(final PaymentRepository repository) {
        final Map<TransitionKey<PaymentState, PaymentEvent>, BaseTransitionActionHandler<
                PaymentState, PaymentEvent, PaymentTransitionKey, PaymentTransitionContext>> handlers = new HashMap<>();
        final List<PaymentTransitionHandler> transitionHandlers = List.of(
                new InitiatePaymentHandler(repository),
                new DismissPaymentIntentHandler(repository),
                new ProcessPaymentHandler(repository),
                new AbortInitiatedPaymentHandler(repository),
                new CompletePaymentHandler(repository),
                new FailPaymentHandler(repository),
                new AbortProcessingPaymentHandler(repository));
        transitionHandlers.forEach(handler -> handler.onTransitionKeys().forEach(key -> {
            if (handlers.putIfAbsent(key, handler) != null) {
                throw new IllegalStateException("Multiple payment handlers declared for " + key);
            }
        }));
        return new PaymentHandlerProvider(handlers);
    }

    private static Transition<PaymentState, PaymentEvent> transition(
            final PaymentState from, final PaymentState to, final PaymentEvent event) {
        return new Transition<>(from, to, event);
    }

    private static final class PaymentHandlerProvider extends TransitionActionHandlerProvider<
            PaymentState, PaymentEvent, PaymentTransitionKey, PaymentTransitionContext> {
        private PaymentHandlerProvider(final Map<TransitionKey<PaymentState, PaymentEvent>,
                BaseTransitionActionHandler<PaymentState, PaymentEvent,
                        PaymentTransitionKey, PaymentTransitionContext>> handlers) {
            super(handlers);
        }
    }
}
