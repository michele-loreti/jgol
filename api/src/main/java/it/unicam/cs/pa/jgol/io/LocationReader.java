package it.unicam.cs.pa.jgol.io;

import it.unicam.cs.pa.jgol.model.Location;

import java.io.IOException;

/**
 * This interface is used to transform a string into a location.
 *
 * @param <C> type of read location.
 */
@FunctionalInterface
public interface LocationReader<C extends Location<C>> {

    /**
     * Returns the location associated with the given string.
     *
     * @param str a string containing a location.
     * @return the location associated with the given string.
     * @throws IOException if the string is not correct.
     */
    C parse(String str) throws IOException;


}
