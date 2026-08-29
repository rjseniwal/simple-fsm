package io.github.cypher.libs.simplefsm.core;

import io.github.cypher.libs.simplefsm.exceptions.SimpleFsmExceptions;
import io.github.cypher.libs.simplefsm.models.Event;
import io.github.cypher.libs.simplefsm.models.State;
import io.github.cypher.libs.simplefsm.models.TransitionContext;
import io.github.cypher.libs.simplefsm.models.TransitionKey;

import java.util.Map;

public abstract class TransitionActionHandlerProvider<S extends State, E extends Event, K extends TransitionKey<S, E>, C extends TransitionContext<S, E, K>> {

    private final Map<TransitionKey<S, E>, BaseTransitionActionHandler<S, E, K, C>> handlers;

    protected TransitionActionHandlerProvider(Map<TransitionKey<S, E>, BaseTransitionActionHandler<S, E, K, C>> handlers) {
        this.handlers = Map.copyOf(handlers);
    }

    public BaseTransitionActionHandler<S, E, K, C> getHandler(C context) {
        K transitionKey = context.getTransitionKey();
        if (!handlers.containsKey(transitionKey)) {
            SimpleFsmExceptions.noHandlerException(transitionKey);
        }
        return handlers.get(transitionKey);
    }
}
