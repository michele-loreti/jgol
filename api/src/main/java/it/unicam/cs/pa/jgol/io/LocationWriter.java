package it.unicam.cs.pa.jgol.io;

import it.unicam.cs.pa.jgol.model.Location;

import java.io.IOException;

/**
 * This interface is used to transform a location into a string.
 *
 * @param <C> type of read location.
 */
@FunctionalInterface
public interface LocationWriter<C extends Location<C>> {

    /**
     * Returns the string representing the given location.
     *
     * @param loc a location.
     * @return the string representing the given location.
     */
    String stringOf(C loc);


}
