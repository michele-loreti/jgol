package it.unicam.cs.pa.jgol.model.conway;

import it.unicam.cs.pa.jgol.model.Rule;

import java.util.List;
import java.util.Optional;

/**
 * Rule implementing the overpopulation rule: Any live cell with more than three live neighbours dies, as if by overpopulation.
 */
public class OverpopulationRule implements Rule<ConwayState> {
    @Override
    public Optional<ConwayState> apply(ConwayState cellStatus, List<ConwayState> neighboursStatus) {
        if (cellStatus == ConwayState.LIVE) {
            if (neighboursStatus.stream().filter(ConwayState::isAlive).count()>3) {
                return Optional.of(ConwayState.DEAD);
            }
        }
        return Optional.empty();
    }
}
