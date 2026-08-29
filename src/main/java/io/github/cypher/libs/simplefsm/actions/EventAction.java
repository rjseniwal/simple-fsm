package io.github.cypher.libs.simplefsm.actions;

import io.github.cypher.libs.simplefsm.models.Event;
import io.github.cypher.libs.simplefsm.models.State;
import io.github.cypher.libs.simplefsm.models.TransitionContext;
import io.github.cypher.libs.simplefsm.models.TransitionKey;

@FunctionalInterface
public interface EventAction<S extends State, E extends Event, K extends TransitionKey<S, E>, C extends TransitionContext<S, E, K>> extends Action<S, E, K, C> {
    void act(C context);
}
