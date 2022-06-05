package it.unicam.cs.pa.jgol.model;

import java.util.Map;

/**
 * This interface is implemented by observers af a GOL execution.
 */
public interface ExecutionListener<S, C extends Location<C>> {

    /**
     * Invoked when an execution step is performed.
     *
     * @param cursor current cursor position.
     * @param updates cell updated in the last step.
     */
    void executionStep(int cursor, Map<C, S> updates);


}
