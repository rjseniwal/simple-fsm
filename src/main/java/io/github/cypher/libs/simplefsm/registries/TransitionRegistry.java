package io.github.cypher.libs.simplefsm.registries;

import io.github.cypher.libs.simplefsm.models.Event;
import io.github.cypher.libs.simplefsm.models.State;
import io.github.cypher.libs.simplefsm.models.Transition;
import io.github.cypher.libs.simplefsm.models.TransitionKey;

import java.util.List;
import java.util.Set;

public interface TransitionRegistry<S extends State, E extends Event, T extends Transition<S, E>> {

    void addTransition(Transition<S, E> key);

    void removeTransition(Transition<S, E> key);

    void addTransitions(List<Transition<S, E>> keys);

    Set<Transition<S, E>> getOutgoingTransitionsFrom(S fromState);
}
