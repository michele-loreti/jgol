package it.unicam.cs.pa.jgol.io;

import it.unicam.cs.pa.jgol.model.CellState;
import it.unicam.cs.pa.jgol.model.Environment;
import it.unicam.cs.pa.jgol.model.Location;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This is a functional interface that is used to read the environment from a String.
 *
 * @param <S>
 * @param <C>
 */
@FunctionalInterface
public interface EnvironmentLoader<S extends CellState, C extends Location<C>> {

    /**
     * Parse a string that contains the description of a GOL environment.
     *
     * @param content string containing the description of the environment.
     * @return the environment associated with the given string.
     * @throws IOException if the string is not well formed.
     */
    Environment<S,C> parse(String content) throws IOException;


    /**
     * Returns the environment stored in the file referenced by the given path.
     *
     * @param path a path to the file containing a GOL environment.
     * @return the environment described in the file referenced by the given path.
     * @throws IOException if an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
     */
    default Environment<S,C>   parse(Path path) throws IOException {
        return parse(Files.readString(path));
    }

    /**
     * Returns the environment stored in the given file.
     *
     * @param file a file containing a GOL environment
     * @return the environment described in the file referenced by the given path.
     * @throws IOException if an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read.
     */
    default Environment<S,C>  parse(File file) throws IOException {
        return parse(file.toPath());
    }

}
