package io.github.cypher.libs.simplefsm.core;

import io.github.cypher.libs.simplefsm.exceptions.SimpleFsmInitException;
import io.github.cypher.libs.simplefsm.models.Event;
import io.github.cypher.libs.simplefsm.models.State;
import io.github.cypher.libs.simplefsm.models.Transition;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StateMachineDefinitionValidatorTest {

    @Test
    void acceptsValidDeterministicGraph() {
        assertDoesNotThrow(() -> definition(
                WorkflowState.CREATED,
                Set.of(WorkflowState.COMPLETED),
                transition(WorkflowState.CREATED, WorkflowState.PROCESSING, WorkflowEvent.START),
                transition(WorkflowState.PROCESSING, WorkflowState.COMPLETED, WorkflowEvent.COMPLETE)));
    }

    @Test
    void rejectsDefinitionWithoutFinalState() {
        assertInvalid(() -> definition(
                WorkflowState.CREATED,
                Set.of(),
                transition(WorkflowState.CREATED, WorkflowState.COMPLETED, WorkflowEvent.START)));
    }

    @Test
    void rejectsTransitionWithNullComponent() {
        assertInvalid(() -> definition(
                WorkflowState.CREATED,
                Set.of(WorkflowState.COMPLETED),
                transition(WorkflowState.CREATED, WorkflowState.COMPLETED, null)));
    }

    @Test
    void rejectsStartOrFinalStateMissingFromGraph() {
        assertInvalid(() -> definition(
                WorkflowState.CREATED,
                Set.of(WorkflowState.CANCELLED),
                transition(WorkflowState.CREATED, WorkflowState.COMPLETED, WorkflowEvent.START)));
    }

    @Test
    void rejectsTwoDestinationsForSameSourceAndEvent() {
        assertInvalid(() -> definition(
                WorkflowState.CREATED,
                Set.of(WorkflowState.COMPLETED, WorkflowState.CANCELLED),
                transition(WorkflowState.CREATED, WorkflowState.COMPLETED, WorkflowEvent.START),
                transition(WorkflowState.CREATED, WorkflowState.CANCELLED, WorkflowEvent.START)));
    }

    @Test
    void rejectsStateThatIsUnreachableFromStart() {
        assertInvalid(() -> definition(
                WorkflowState.CREATED,
                Set.of(WorkflowState.COMPLETED, WorkflowState.CANCELLED),
                transition(WorkflowState.CREATED, WorkflowState.COMPLETED, WorkflowEvent.COMPLETE),
                transition(WorkflowState.PROCESSING, WorkflowState.CANCELLED, WorkflowEvent.CANCEL)));
    }

    @Test
    void rejectsReachableNonFinalStateWithoutPathToFinalState() {
        assertInvalid(() -> definition(
                WorkflowState.CREATED,
                Set.of(WorkflowState.COMPLETED),
                transition(WorkflowState.CREATED, WorkflowState.PROCESSING, WorkflowEvent.START),
                transition(WorkflowState.PROCESSING, WorkflowState.CANCELLED, WorkflowEvent.RETRY),
                transition(WorkflowState.CANCELLED, WorkflowState.PROCESSING, WorkflowEvent.CANCEL),
                transition(WorkflowState.CREATED, WorkflowState.COMPLETED, WorkflowEvent.COMPLETE)));
    }

    @Test
    void rejectsOutgoingTransitionFromFinalState() {
        assertInvalid(() -> definition(
                WorkflowState.CREATED,
                Set.of(WorkflowState.COMPLETED),
                transition(WorkflowState.CREATED, WorkflowState.COMPLETED, WorkflowEvent.COMPLETE),
                transition(WorkflowState.COMPLETED, WorkflowState.PROCESSING, WorkflowEvent.RETRY),
                transition(WorkflowState.PROCESSING, WorkflowState.COMPLETED, WorkflowEvent.COMPLETE)));
    }

    @SafeVarargs
    private static StateMachineDefinition<WorkflowState, WorkflowEvent> definition(
            final WorkflowState startState,
            final Set<WorkflowState> finalStates,
            final Transition<WorkflowState, WorkflowEvent>... transitions) {
        return new StateMachineDefinition<>(startState, finalStates, Set.of(transitions));
    }

    private static Transition<WorkflowState, WorkflowEvent> transition(
            final WorkflowState from,
            final WorkflowState to,
            final WorkflowEvent event) {
        return new Transition<>(from, to, event);
    }

    private static void assertInvalid(final Runnable construction) {
        assertThrows(SimpleFsmInitException.class, construction::run);
    }

    private enum WorkflowState implements State {
        CREATED, PROCESSING, COMPLETED, CANCELLED
    }

    private enum WorkflowEvent implements Event {
        START, COMPLETE, CANCEL, RETRY
    }
}
