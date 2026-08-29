package io.github.cypher.libs.simplefsm.core;

import io.github.cypher.libs.simplefsm.actions.Action;
import io.github.cypher.libs.simplefsm.actions.ErrorAction;
import io.github.cypher.libs.simplefsm.actions.EventAction;
import io.github.cypher.libs.simplefsm.models.Event;
import io.github.cypher.libs.simplefsm.models.State;
import io.github.cypher.libs.simplefsm.models.TransitionContext;
import io.github.cypher.libs.simplefsm.models.TransitionKey;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseStateMachine<S extends State, E extends Event, K extends TransitionKey<S, E>, C extends TransitionContext<S, E, K>> {

    private final StateEngine<S, E, K, C> stateEngine;

    protected BaseStateMachine(@NonNull final StateMachineDefinition<S, E> stateMachineDefinition,
                               @NonNull final TransitionActionHandlerProvider<S, E, K, C> transitionActionHandlerProvider) {
        this(stateMachineDefinition, transitionActionHandlerProvider, Action.loggingErrorAction(),
                Action.noOpEventAction(), Action.noOpEventAction());
    }

    protected BaseStateMachine(@NonNull final StateMachineDefinition<S, E> stateMachineDefinition,
                               @NonNull final TransitionActionHandlerProvider<S, E, K, C> transitionActionHandlerProvider,
                               @NonNull final ErrorAction<S, E, K, C> defaultErrorAction,
                               @NonNull final EventAction<S, E, K, C> defaultPreEventAction,
                               @NonNull final EventAction<S, E, K, C> defaultPostEventAction) {
        this.stateEngine = new StateEngine<>(stateMachineDefinition, transitionActionHandlerProvider,
                defaultErrorAction, defaultPreEventAction, defaultPostEventAction);
    }

    public void fire(@NonNull final C context) {
        stateEngine.handleTransition(context);
    }
}
