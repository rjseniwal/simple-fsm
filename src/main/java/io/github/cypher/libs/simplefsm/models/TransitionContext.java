package io.github.cypher.libs.simplefsm.models;

import io.github.cypher.libs.simplefsm.exceptions.SimpleFsmException;
import io.github.cypher.libs.simplefsm.exceptions.SimpleFsmExceptions;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Getter
public abstract class TransitionContext<S extends State, E extends Event, T extends TransitionKey<S, E>> {
    private final TransitionRequest<S, E> transitionRequest;
    private final T transitionKey;
    private final Map<String, Object> metaData;

    protected TransitionContext(TransitionRequest<S, E> transitionRequest) {
        this.transitionRequest = transitionRequest;
        this.transitionKey = buildTransitionKey(transitionRequest);
        if (!Objects.equals(transitionRequest.fromState(), transitionKey.getFromState()) ||
                !Objects.equals(transitionRequest.event(), transitionKey.getCausedEvent())) {
            throw new SimpleFsmException("Transition request and key are not synonymous, hinting at incorrect transition key build code.");
        }
        this.metaData = new HashMap<>();
    }

    protected abstract T buildTransitionKey(TransitionRequest<S, E> transitionRequest);

    public void addMeta(String key, Object value) {
        metaData.put(key, value);
    }

    // Override for safe casting via object mapper if needed
    public <V> Optional<V> getMetaData(String key, Class<V> vClass) {
        return Optional.ofNullable(metaData.get(key))
                .map(vClass::cast);
    }
}
