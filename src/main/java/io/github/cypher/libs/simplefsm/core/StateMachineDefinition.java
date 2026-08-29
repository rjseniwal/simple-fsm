package io.github.cypher.libs.simplefsm.core;

import io.github.cypher.libs.simplefsm.exceptions.SimpleFsmExceptions;
import io.github.cypher.libs.simplefsm.exceptions.SimpleFsmInitException;
import io.github.cypher.libs.simplefsm.models.*;

import java.util.Objects;
import java.util.Set;

public record StateMachineDefinition<S extends State, E extends Event>(
        S startState, Set<S> finalStates, Set<Transition<S, E>> transitions) {

    public StateMachineDefinition(final S startState,
                                  final Set<S> finalStates,
                                  final Set<Transition<S, E>> transitions) {
        if (Objects.isNull(startState) || Objects.isNull(finalStates) || Objects.isNull(transitions)) {
            SimpleFsmExceptions.initException(SimpleFsmInitException.InitFailureReasons.BAD_INIT_PARAMS);
        }
        this.startState = startState;
        this.finalStates = Set.copyOf(finalStates);
        this.transitions = Set.copyOf(transitions);
        StateMachineDefinitionValidator.validateStateMachineDefinition(this);
    }

    public static <S extends State, E extends Event> StateMachineDefinition<S, E> from(StateMachineBuilder<S, E> builder) {
        return new StateMachineDefinition<>(builder.getStartState(), Set.copyOf(builder.getFinalStates()), Set.copyOf(builder.getTransitions()));
    }
}
