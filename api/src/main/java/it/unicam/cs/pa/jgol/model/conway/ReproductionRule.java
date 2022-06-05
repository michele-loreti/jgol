package it.unicam.cs.pa.jgol.model.conway;

import it.unicam.cs.pa.jgol.model.Rule;

import java.util.List;
import java.util.Optional;

/**
 * Rule implementing the reproduction rule: Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
 */
public class ReproductionRule implements Rule<ConwayState> {
    @Override
    public Optional<ConwayState> apply(ConwayState cellStatus, List<ConwayState> neighboursStatus) {
        if (cellStatus == ConwayState.DEAD) {
            if (neighboursStatus.stream().filter(ConwayState::isAlive).count()==3) {
                return Optional.of(ConwayState.LIVE);
            }
        }
        return Optional.empty();
    }
}
