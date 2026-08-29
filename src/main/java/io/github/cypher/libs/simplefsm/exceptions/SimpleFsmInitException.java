package io.github.cypher.libs.simplefsm.exceptions;

import lombok.Getter;

@Getter
public class SimpleFsmInitException extends SimpleFsmException {
    public enum InitFailureReasons {
        DUPLICATE_TRANSITION("An action handler already exists for the transition key, duplicate actions are not allowed."),
        BAD_INIT_PARAMS("One or more initializtaion parameters are passed in as null."),
        INVALID_STATE_MACHINE_DEFINITION("Invalid State Machine definition.");

        @Getter
        private final String reasonDetail;

        InitFailureReasons(String reasonDetail) {
            this.reasonDetail = reasonDetail;
        }
    }

    private final InitFailureReasons reason;

    public SimpleFsmInitException(InitFailureReasons reason) {
        super(reason.getReasonDetail());
        this.reason = reason;
    }
}
