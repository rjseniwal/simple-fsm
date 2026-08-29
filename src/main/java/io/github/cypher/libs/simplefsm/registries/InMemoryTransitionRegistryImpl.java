package io.github.cypher.libs.simplefsm.registries;

import io.github.cypher.libs.simplefsm.models.Event;
import io.github.cypher.libs.simplefsm.models.State;
import io.github.cypher.libs.simplefsm.models.Transition;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTransitionRegistryImpl<S extends State, E extends Event, T extends Transition<S, E>>
        implements TransitionRegistry<S, E, T> {

    private final Map<S, Set<Transition<S, E>>> transitions;

    public InMemoryTransitionRegistryImpl() {
        this.transitions = new ConcurrentHashMap<>();
    }

    @Override
    public void addTransition(Transition<S, E> transition) {
        this.transitions.computeIfAbsent(transition.getFromState(), k -> new HashSet<Transition<S, E>>())
                .add(transition);
    }

    @Override
    public void removeTransition(Transition<S, E> transition) {
        this.transitions.computeIfAbsent(transition.getFromState(), k -> new HashSet<Transition<S, E>>())
                .remove(transition);
    }

    @Override
    public void addTransitions(List<Transition<S, E>> transitions) {
        if (transitions != null && !transitions.isEmpty()) {
            transitions.stream()
                    .forEach(this::addTransition);
        }
    }

    @Override
    public Set<Transition<S, E>> getOutgoingTransitionsFrom(S fromState) {
        return transitions.getOrDefault(fromState, new HashSet<>());
    }
}
