package io.github.cypher.libs.simplefsm.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SimpleFsmException extends RuntimeException {
    public SimpleFsmException(String message) {
        super(message);
    }
}
