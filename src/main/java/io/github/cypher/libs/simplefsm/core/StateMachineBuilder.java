package io.github.cypher.libs.simplefsm.core;

import io.github.cypher.libs.simplefsm.exceptions.SimpleFsmExceptions;
import io.github.cypher.libs.simplefsm.exceptions.SimpleFsmInitException;
import io.github.cypher.libs.simplefsm.models.*;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Getter
public class StateMachineBuilder<S extends State, E extends Event> {

    private final S startState;
    private final Set<S> finalStates;
    private final Set<Transition<S, E>> transitions = new HashSet<>();

    public StateMachineBuilder(final S startState,
                               final Set<S> finalStates) {
        this.startState = startState;
        this.finalStates = finalStates;
    }

    public StateMachineDefinition<S, E> build() {
        final var machineDefinition = StateMachineDefinition.from(this);
        StateMachineDefinitionValidator.validateStateMachineDefinition(machineDefinition);
        return machineDefinition;
    }

    public StateMachineBuilder<S, E> addTransition(@NonNull Transition<S, E> transition) {
        if (this.transitions.contains(transition)) {
            SimpleFsmExceptions.initException(SimpleFsmInitException.InitFailureReasons.DUPLICATE_TRANSITION);
        }
        this.transitions.add(transition);
        return this;
    }
}
