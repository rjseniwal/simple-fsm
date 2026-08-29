package io.github.cypher.libs.simplefsm.models;

import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Transition<S extends State, E extends Event> {
    private S fromState;
    private S toState;
    private E causedEvent;

    @Override
    public String toString() {
        return String.format("{from: %s, to: %s, event: %s}", fromState, toState, causedEvent);
    }
}
