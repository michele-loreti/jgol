package it.unicam.cs.pa.jgol.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This functional interface is used to compute the next state of a cell give its current state and
 * the states of all its neighbours.
 *
 * @param <S> cell state
 */
@FunctionalInterface
public interface Rule<S> {

    /**
     * Returns the result of the application of this rule to a cell give its current state and the states of all its neighbours.
     * If the rule cannot be applied in this configuration, an empty {@link Optional<S>} is returned.
     *
     * @param cellStatus the state of a cell
     * @param neighboursStatus the states of the neighbours
     * @return the next state of a cell give its current state and the states of all its neighbours.
     */
    Optional<S> apply(S cellStatus, List<S> neighboursStatus);

    /**
     * Returns the result of the application of this rule to a cell at the given location whose state is obtained by the given
     * function <code>locationSolver</code>.
     *
     * @param loc a location.
     * @param locationSolver function used to associate each location with the state of if cell.
     * @return the result of the application of this rule to a cell at the given location.
     * @param <C> type of locations.
     */
    default <C extends Location<C>> Optional<S> apply(C loc, Function<? super C, ? extends S> locationSolver) {
        return apply(locationSolver.apply(loc), loc.neighbours().stream().map(locationSolver).collect(Collectors.toList()));
    }

    /**
     * Returns the next state of a cell given its current state and the states of its neighbour obtained by applying this rule.
     * If this rule cannot be applied, the cell current state is returned.
     *
     * @param cellStatus the state of a cell
     * @param neighboursStatus the states of the neighbours
     * @return the next state of a cell give its current state and the states of all its neighbours.
     */
    default S next(S cellStatus, List<S> neighboursStatus) {
        return apply(cellStatus, neighboursStatus).orElse(cellStatus);
    }

}
