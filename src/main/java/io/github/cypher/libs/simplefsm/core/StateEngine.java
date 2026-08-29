package io.github.cypher.libs.simplefsm.core;

import io.github.cypher.libs.simplefsm.actions.ErrorAction;
import io.github.cypher.libs.simplefsm.actions.EventAction;
import io.github.cypher.libs.simplefsm.exceptions.SimpleFsmExceptions;
import io.github.cypher.libs.simplefsm.models.*;
import lombok.NonNull;

public class StateEngine<S extends State, E extends Event, K extends TransitionKey<S, E>, C extends TransitionContext<S, E, K>> {

    private final StateMachineDefinition<S, E> stateMachineDefinition;
    private final ErrorAction<S, E, K, C> defaultErrorAction;
    private final EventAction<S, E, K, C> defaultPreEventAction;
    private final EventAction<S, E, K, C> defaultOnEventAction;
    private final EventAction<S, E, K, C> defaultPostEventAction;

    public StateEngine(final StateMachineDefinition<S, E> stateMachineDefinition,
                       final TransitionActionHandlerProvider<S, E, K, C> transitionActionHandlerProvider,
                       final ErrorAction<S, E, K, C> defaultErrorAction,
                       final EventAction<S, E, K, C> defaultPreEventAction,
                       final EventAction<S, E, K, C> defaultPostEventAction) {
        this.stateMachineDefinition = stateMachineDefinition;
        this.defaultErrorAction = defaultErrorAction;
        this.defaultPreEventAction = defaultPreEventAction;
        this.defaultOnEventAction = context -> {
            final var handler = transitionActionHandlerProvider.getHandler(context);
            handler.handle(context);
        };
        this.defaultPostEventAction = defaultPostEventAction;
    }

    public void handleTransition(@NonNull final C context) {
        try {
            validateTransitionExists(context);
            handlePreProcessing(context);
            handleTransitionInternal(context);
            handlePostProcessing(context);
        } catch (Exception original) {
            try {
                defaultErrorAction.act(context, original);
            } catch (Exception callbackFailure) {
                original.addSuppressed(callbackFailure);
            }
            throw original;
        }
    }

    private void validateTransitionExists(final C context) {
        final var transition = new Transition<>(context.getTransitionRequest().fromState(), context.getTransitionRequest().targetState(), context.getTransitionRequest().event());
        if (!stateMachineDefinition.transitions().contains(transition)) {
            SimpleFsmExceptions.badTransitionException(transition);
        }
    }

    private void handleTransitionInternal(C context) {
        defaultOnEventAction.act(context);
    }

    private void handlePostProcessing(C context) {
        defaultPostEventAction.act(context);
    }

    private void handlePreProcessing(C context) {
        defaultPreEventAction.act(context);
    }

}
