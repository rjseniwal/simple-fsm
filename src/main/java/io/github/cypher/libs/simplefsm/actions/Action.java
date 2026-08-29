package io.github.cypher.libs.simplefsm.actions;

import io.github.cypher.libs.simplefsm.models.Event;
import io.github.cypher.libs.simplefsm.models.State;
import io.github.cypher.libs.simplefsm.models.TransitionContext;
import io.github.cypher.libs.simplefsm.models.TransitionKey;

public interface Action<S extends State, E extends Event, K extends TransitionKey<S, E>, C extends TransitionContext<S, E, K>> {

    static <S extends State, E extends Event, K extends TransitionKey<S, E>, C extends TransitionContext<S, E, K>> ErrorAction<S, E, K, C> loggingErrorAction() {
        return new LoggingErrorAction<>();
    }

    static <S extends State, E extends Event, K extends TransitionKey<S, E>, C extends TransitionContext<S, E, K>> EventAction<S, E, K, C> noOpEventAction() {
        return new NoOpEventAction<>();
    }
}
