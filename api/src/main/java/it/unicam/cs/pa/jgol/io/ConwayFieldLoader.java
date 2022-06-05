package it.unicam.cs.pa.jgol.io;

import it.unicam.cs.pa.jgol.model.Environment;
import it.unicam.cs.pa.jgol.model.Location;
import it.unicam.cs.pa.jgol.model.conway.ConwayField;
import it.unicam.cs.pa.jgol.model.conway.ConwayState;

import java.io.IOException;

/**
 * A loader used to load a ConwayModel. This loader assumes that the live cells are listed, one per line in the file.
 */
public class ConwayFieldLoader<C extends Location<C>> implements EnvironmentLoader<ConwayState, C> {

    private final LocationReader<C> lineParser;


    /**
     * Creates a loader that parses each line with the given function.
     *
     * @param lineParser parser used to read a single location.
     */
    public ConwayFieldLoader(LocationReader<C>  lineParser) {
        this.lineParser = lineParser;
    }

    @Override
    public Environment<ConwayState,C>  parse(String content) throws IOException {
        ConwayField<C> field = new ConwayField<>();
        String[] lines = content.split("\n");
        for(int i=0; i<lines.length; i++) {
            try {
                field.setAlive( lineParser.parse(lines[i]) );

            } catch (IOException e) {
                throw new IOException("Syntax error at line "+i, e);
            }
        }
        return field;
    }
}
