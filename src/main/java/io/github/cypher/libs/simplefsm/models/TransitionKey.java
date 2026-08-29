package io.github.cypher.libs.simplefsm.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class TransitionKey<S extends State, E extends Event> {
    private final S fromState;
    private final E causedEvent;

    protected TransitionKey(final S fromState,
                            final E causedEvent) {
        this.fromState = fromState;
        this.causedEvent = causedEvent;
    }

    @Override
    public String toString() {
        return String.format("{from: %s | onEvent: %s}", fromState, causedEvent);
    }
}