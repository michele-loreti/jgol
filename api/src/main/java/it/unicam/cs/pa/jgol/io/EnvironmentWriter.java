package it.unicam.cs.pa.jgol.io;

import it.unicam.cs.pa.jgol.model.CellState;
import it.unicam.cs.pa.jgol.model.Environment;
import it.unicam.cs.pa.jgol.model.Location;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This is a functional interface that is used to write the environment into a String.
 *
 * @param <S>
 * @param <C>
 */
@FunctionalInterface
public interface EnvironmentWriter<S extends CellState, C extends Location<C>> {


    /**
     * Returns the string representing the given field.
     *
     * @param field a field.
     * @return the string representing the given field.
     */
    String stringOf(Environment<S, C> field);


    /**
     * Writes the given field in the file referenced by the given path.
     *
     * @param path the path where the field is saved.
     * @param field the field to write.
     * @throws IOException if an I/O error occurs while writing the file.
     */
    default void  writeTo(Path path, Environment<S, C> field) throws IOException {
        Files.write(path, stringOf(field).getBytes());
    }

    /**
     * Writes the given field in the given file.
     *
     * @param file the file where the field is saved.
     * @param field the field to write.
     * @throws IOException if an I/O error occurs while writing the file.
     */
    default void  writeTo(File file, Environment<S, C> field) throws IOException {
        writeTo(file.toPath(), field);
    }

}
