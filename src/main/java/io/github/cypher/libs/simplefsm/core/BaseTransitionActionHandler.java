package io.github.cypher.libs.simplefsm.core;

import io.github.cypher.libs.simplefsm.models.Event;
import io.github.cypher.libs.simplefsm.models.State;
import io.github.cypher.libs.simplefsm.models.TransitionContext;
import io.github.cypher.libs.simplefsm.models.TransitionKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public abstract class BaseTransitionActionHandler<S extends State, E extends Event, T extends TransitionKey<S, E>, C extends TransitionContext<S, E, T>> {

    public abstract void handle(C context);

    public abstract Set<TransitionKey<S, E>> onTransitionKeys();
}
