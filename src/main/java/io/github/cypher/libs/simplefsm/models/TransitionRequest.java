package io.github.cypher.libs.simplefsm.models;

public record TransitionRequest<S extends State, E extends Event>(S fromState, E event, S targetState) {

    @Override
    public String toString() {
        return String.format("{from: %s, to: %s, event: %s}", fromState, targetState, event);
    }
}
