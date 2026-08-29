package io.github.cypher.libs.simplefsm.exceptions;

import io.github.cypher.libs.simplefsm.models.Transition;

public class BadTransitionException extends SimpleFsmException {
    public BadTransitionException(Transition<?, ?> transition) {
        super(String.format("The requested transition: %s is invalid.", transition));
    }
}
