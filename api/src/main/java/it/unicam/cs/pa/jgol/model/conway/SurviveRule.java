package it.unicam.cs.pa.jgol.model.conway;

import it.unicam.cs.pa.jgol.model.Rule;
import it.unicam.cs.pa.jgol.model.conway.ConwayState;

import java.util.List;
import java.util.Optional;

/**
 * Rule implementing the surviving rule: Any live cell with two or three live neighbours lives on to the next generation.
 */
public class SurviveRule implements Rule<ConwayState> {
    @Override
    public Optional<ConwayState> apply(ConwayState cellStatus, List<ConwayState> neighboursStatus) {
        if (cellStatus == ConwayState.LIVE) {
            long counter = neighboursStatus.stream().filter(ConwayState::isAlive).count();
            if ((counter==2)||(counter==3)) {
                return Optional.of(ConwayState.LIVE);
            }
        }
        return Optional.empty();
    }
}
