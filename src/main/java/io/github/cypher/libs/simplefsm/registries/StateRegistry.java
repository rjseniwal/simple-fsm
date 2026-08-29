package io.github.cypher.libs.simplefsm.registries;

import io.github.cypher.libs.simplefsm.models.State;

import java.util.Set;

public interface StateRegistry<S extends State> {

    S getStartState();

    void setStartState(final S startState);

    Set<S> getAllStates();

    Set<S> getFinalStates();

    void setFinalStates(Set<S> finalStates);
}
