package io.github.cypher.libs.simplefsm.registries;

import io.github.cypher.libs.simplefsm.models.State;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class InMemoryStateRegistryImpl<S extends State> implements StateRegistry<S> {

    private AtomicReference<S> startState;
    private AtomicReference<Set<S>> finalStates;

    @Override
    public S getStartState() {
        return startState.get();
    }

    @Override
    public void setStartState(final S startState) {
        this.startState.set(startState);
    }

    @Override
    public Set<S> getAllStates() {
        return Set.of();
    }

    @Override
    public Set<S> getFinalStates() {
        return finalStates.get();
    }

    @Override
    public void setFinalStates(Set<S> finalStates) {
        this.finalStates.set(finalStates);
    }
}
