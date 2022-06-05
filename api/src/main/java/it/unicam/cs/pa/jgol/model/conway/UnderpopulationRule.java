package it.unicam.cs.pa.jgol.model.conway;

import it.unicam.cs.pa.jgol.model.Rule;
import it.unicam.cs.pa.jgol.model.conway.ConwayState;

import java.util.List;
import java.util.Optional;

/**
 * Rule implementing the underpopulation rule: Any live cell with fewer than two live neighbours dies, as if by underpopulation.
 */
public class UnderpopulationRule implements Rule<ConwayState> {

    @Override
    public Optional<ConwayState> apply(ConwayState cellStatus, List<ConwayState> neighboursStatus) {
        if (cellStatus == ConwayState.LIVE) {
            if (neighboursStatus.stream().filter(ConwayState::isAlive).count()<2) {
                return Optional.of(ConwayState.DEAD);
            }
        }
        return Optional.empty();
    }

}
