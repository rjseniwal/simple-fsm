package io.github.cypher.libs.simplefsm.core;

import io.github.cypher.libs.simplefsm.exceptions.SimpleFsmInitException;
import io.github.cypher.libs.simplefsm.models.Event;
import io.github.cypher.libs.simplefsm.models.State;
import io.github.cypher.libs.simplefsm.models.Transition;
import lombok.experimental.UtilityClass;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@UtilityClass
public class StateMachineDefinitionValidator {

    public static <S extends State, E extends Event> void validateStateMachineDefinition(
            final StateMachineDefinition<S, E> stateMachineDefinition) {
        if (stateMachineDefinition == null
                || stateMachineDefinition.startState() == null
                || stateMachineDefinition.finalStates() == null
                || stateMachineDefinition.finalStates().isEmpty()
                || stateMachineDefinition.finalStates().stream().anyMatch(Objects::isNull)
                || stateMachineDefinition.transitions() == null) {
            throw invalidDefinition();
        }

        final S startState = stateMachineDefinition.startState();
        final Set<S> finalStates = stateMachineDefinition.finalStates();
        final Set<S> graphStates = new HashSet<>();
        final Map<S, Set<S>> adjacency = new HashMap<>();
        final Map<S, Set<S>> reverseAdjacency = new HashMap<>();
        final Map<S, Set<E>> eventsBySourceState = new HashMap<>();

        for (final Transition<S, E> transition : stateMachineDefinition.transitions()) {
            if (transition == null
                    || transition.getFromState() == null
                    || transition.getToState() == null
                    || transition.getCausedEvent() == null) {
                throw invalidDefinition();
            }

            final S fromState = transition.getFromState();
            final S toState = transition.getToState();
            final E event = transition.getCausedEvent();

            if (!eventsBySourceState.computeIfAbsent(fromState, ignored -> new HashSet<>()).add(event)) {
                throw invalidDefinition();
            }

            graphStates.add(fromState);
            graphStates.add(toState);
            adjacency.computeIfAbsent(fromState, ignored -> new HashSet<>()).add(toState);
            adjacency.computeIfAbsent(toState, ignored -> new HashSet<>());
            reverseAdjacency.computeIfAbsent(toState, ignored -> new HashSet<>()).add(fromState);
            reverseAdjacency.computeIfAbsent(fromState, ignored -> new HashSet<>());
        }

        if (!graphStates.contains(startState) || !graphStates.containsAll(finalStates)) {
            throw invalidDefinition();
        }

        for (final S finalState : finalStates) {
            if (!adjacency.getOrDefault(finalState, Set.of()).isEmpty()) {
                throw invalidDefinition();
            }
        }

        final Set<S> reachableFromStart = traverseFrom(Set.of(startState), adjacency);
        if (!reachableFromStart.containsAll(graphStates)) {
            throw invalidDefinition();
        }

        final Set<S> statesReachingAFinalState = traverseFrom(finalStates, reverseAdjacency);
        for (final S reachableState : reachableFromStart) {
            if (!finalStates.contains(reachableState)
                    && !statesReachingAFinalState.contains(reachableState)) {
                throw invalidDefinition();
            }
        }
    }

    private static <S> Set<S> traverseFrom(final Set<S> starts, final Map<S, Set<S>> adjacency) {
        final Set<S> visited = new HashSet<>();
        final ArrayDeque<S> pending = new ArrayDeque<>(starts);

        while (!pending.isEmpty()) {
            final S state = pending.removeFirst();
            if (visited.add(state)) {
                pending.addAll(adjacency.getOrDefault(state, Set.of()));
            }
        }
        return visited;
    }

    private static SimpleFsmInitException invalidDefinition() {
        return new SimpleFsmInitException(
                SimpleFsmInitException.InitFailureReasons.INVALID_STATE_MACHINE_DEFINITION);
    }
}
