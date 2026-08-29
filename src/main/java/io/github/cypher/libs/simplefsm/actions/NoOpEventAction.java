package io.github.cypher.libs.simplefsm.actions;

import io.github.cypher.libs.simplefsm.models.Event;
import io.github.cypher.libs.simplefsm.models.State;
import io.github.cypher.libs.simplefsm.models.TransitionContext;
import io.github.cypher.libs.simplefsm.models.TransitionKey;

public class NoOpEventAction<S extends State, E extends Event, K extends TransitionKey<S, E>, C extends TransitionContext<S, E, K>> implements EventAction<S, E, K, C> {
    @Override
    public void act(C context) {
        // no-op
    }
}
