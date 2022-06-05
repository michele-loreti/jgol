package it.unicam.cs.pa.jgol.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Classes implementing this interface are used to represent the environment where the cells
 * live.
 *
 * @param <S> type representing the status of cells.
 * @param <C> type representing the position of cells in the environment.
 */
public interface Environment<S extends CellState, C extends Location<C>> {

    /**
     * Returns the state of the cell located at the given location.
     *
     * @param loc a location.
     * @return the status of the cell located at the given location.
     */
    S statusOf(C loc);


    /**
     * Given a set of locations, returns the map associating to each of its state.
     *
     * @param locs a set of locations.
     * @return the map associating to each of the location in the given set with its  state.
     */
    Map<C,S> getStatusMapOf(Collection<C> locs);

    /**
     * Returns a list containing the states of the cells at the given locations.
     *
     * @param locs a collection of locations.
     * @return a list containing the states of the cells at the given locations.
     */
    List<S> statusOf(Collection<C> locs);


    /**
     * Returns a map containing the cells that are changing their state due to the application of the given rule.
     *
     * @param rule a rule describing the evolution of the environment.
     * @return the environment obtained from this one by applying the given rule.
     */
    Map<C, S> evolvingCells(Rule<S> rule);


    /**
     * Returns the environment obtained from this one by applying the given rule.
     *
     * @param rule  a rule describing the evolution of the environment.
     * @return  the environment obtained from this one by applying the given rule.
     */
    default Environment<S, C> apply(Rule<S> rule) {
        return set(evolvingCells(rule));
    }

    /**
     * Returns the environment obtained by this one by changing the state of the cells according to the given map.
     *
     * @param updates a mappinc associating each location with its new state.
     * @return the environment obtained by this one by changing the state of the cells according to the given map.
     */
    Environment<S, C> set(Map<C, S> updates);


    /**
     * Sets the state of a cell at a given location.
     *
     * @param loc cell location.
     * @param state cell state.
     */
    void set(C loc, S state);

    /**
     * Returns the list of locations containing cells with the given state. Whenever <code>state.isDead()</code> the
     * returning list will be empty. This because, when the environment is infinite, only cells that
     * are not dead will be tracked.
     *
     * @param state a state of the cell.
     * @return the list of locations containing cells with the given state.
     */
    List<C> getLocations(S state);

}
