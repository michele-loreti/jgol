package it.unicam.cs.pa.jgol.io;

import it.unicam.cs.pa.jgol.model.Environment;
import it.unicam.cs.pa.jgol.model.Location;
import it.unicam.cs.pa.jgol.model.conway.ConwayState;

import java.util.stream.Collectors;

public class ConwayFieldWriter<C extends Location<C>> implements EnvironmentWriter<ConwayState, C> {

    private final LocationWriter<C> locationWriter;

    /**
     * Creates a writer for Conway fields.
     *
     * @param locationWriter writer used to serialize the used location.
     */
    public ConwayFieldWriter(LocationWriter<C> locationWriter) {
        this.locationWriter = locationWriter;
    }


    @Override
    public String stringOf(Environment<ConwayState,C> field) {
        return field.getLocations(ConwayState.LIVE).stream().map(locationWriter::stringOf).collect(Collectors.joining("\n"));
    }

}
