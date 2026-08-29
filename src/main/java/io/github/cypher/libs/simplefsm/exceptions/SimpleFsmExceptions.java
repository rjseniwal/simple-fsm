package io.github.cypher.libs.simplefsm.exceptions;

import io.github.cypher.libs.simplefsm.models.Transition;
import io.github.cypher.libs.simplefsm.models.TransitionKey;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SimpleFsmExceptions {

    public static void initException(SimpleFsmInitException.InitFailureReasons reason) {
        throw new SimpleFsmInitException(reason);
    }

    public static void noHandlerException(TransitionKey<?, ?> transitionKey) {
        throw new NoHandlerException(transitionKey);
    }

    public static void badTransitionException(final Transition<?, ?> transition) {
        throw new BadTransitionException(transition);
    }
}
