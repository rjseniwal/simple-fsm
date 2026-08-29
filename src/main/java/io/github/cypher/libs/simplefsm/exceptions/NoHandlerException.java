package io.github.cypher.libs.simplefsm.exceptions;

import io.github.cypher.libs.simplefsm.models.TransitionKey;
import lombok.Getter;

@Getter
public class NoHandlerException extends SimpleFsmException {
    private final TransitionKey<?, ?> transitionKey;

    public NoHandlerException(TransitionKey<?, ?> transitionKey) {
        super(String.format("No handler found for transition key: %s", transitionKey));
        this.transitionKey = transitionKey;
    }
}
