package io.github.cypher.libs.simplefsm.actions;

import io.github.cypher.libs.simplefsm.models.Event;
import io.github.cypher.libs.simplefsm.models.State;
import io.github.cypher.libs.simplefsm.models.TransitionContext;
import io.github.cypher.libs.simplefsm.models.TransitionKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingErrorAction<S extends State, E extends Event, K extends TransitionKey<S, E>, C extends TransitionContext<S, E, K>> implements ErrorAction<S, E, K, C> {

    @Override
    public void act(final C context,
                    final Exception e) {
        log.error("Received exception while processing transition request: {}.",
                context.getTransitionRequest(), e);
    }
}
