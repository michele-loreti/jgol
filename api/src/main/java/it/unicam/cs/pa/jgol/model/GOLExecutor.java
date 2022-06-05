package it.unicam.cs.pa.jgol.model;

import it.unicam.cs.pa.jgol.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is used to execute a Game of Life model.
 *
 * @param <S> type of cell status.
 * @param <C> type of cell locations.
 */
public final class GOLExecutor<S extends CellState,C extends Location<C>> {

    private Environment<S, C> field;

    private final ArrayList<Pair<Map<C,S>, Map<C,S>>> updates = new ArrayList<>();

    private int cursor = 0;
    private final Rule<S> rule;

    private final List<ExecutionListener<S,C>> listenerList;

    /**
     * Creates a new executor starting from the given field.
     *
     * @param field starting configuration of this field.
     * @param rule rule used in the executor.
     */
    public GOLExecutor(Environment<S,C> field, Rule<S> rule) {
        this.field = field;
        this.rule = rule;
        this.listenerList = new LinkedList<>();
    }

    /**
     * Adds a listener to this execution.
     *
     * @param listener the listener to add.
     */
    public synchronized void addExecutionListener(ExecutionListener<S,C> listener) {
        this.listenerList.add(listener);
    }

    /**
     * Removes the given listener from the the listeners of this execution.
     *
     * @param listener the listener to remove.
     */
    public synchronized void removeExecutionListener(ExecutionListener<S,C> listener) {
        this.listenerList.remove(listener);
    }

    /**
     * Returns the length of this execution.
     *
     * @return the length of this execution.
     */
    public int length() {
        return updates.size();
    }

    /**
     * Returns the position of the cursor in this execution.
     *
     * @return the position of the cursor in this execution.
     */
    public int cursorPosition() {
        return cursor;
    }

    /**
     * Returns the field selected by the cursor.
     *
     * @return the field selected by the cursor.
     */
    public synchronized Environment<S,C> getField() {
        return this.field;
    }

    /**
     * Performs a step forward.
     */
    public synchronized void stepForward() {
        if (cursor<updates.size()) {
            updateField(updates.get(cursor++).getFirst());
        } else {
            computeNextStep();
        }
    }

    /**
     * Performs a backward step.
     */
    public synchronized void stepBackward() {
        if (cursor>0) {
            updateField(updates.get(cursor--).getSecond());
        }
    }

    private void computeNextStep() {
        Map<C, S> forwardUpdate = field.evolvingCells(this.rule);
        Map<C, S> backwardUpdate = field.getStatusMapOf(forwardUpdate.keySet());
        this.updates.add(new Pair<>(forwardUpdate, backwardUpdate));
        this.cursor++;
        updateField(forwardUpdate);
    }

    private synchronized void updateField(Map<C,S> updates) {
        this.field = this.field.set(updates);
        this.listenerList.forEach(lst -> lst.executionStep(this.cursor, updates));
    }
}
