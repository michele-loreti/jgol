package it.unicam.cs.pa.jgol.model.conway;

import it.unicam.cs.pa.jgol.model.ApplyFirst;
import it.unicam.cs.pa.jgol.model.CellState;
import it.unicam.cs.pa.jgol.model.Rule;

import java.io.Serializable;

/**
 * Enumeration used to represent the state of a cell in the Conway model.
 *
 */
public enum ConwayState implements CellState {
    /**
     * The state of a dead cell.
     */
    DEAD,
    /**
     * The state of a living cell.
     */
    LIVE;

    /**
     * Default rules for Conway models.
     */
    public final static Rule<ConwayState> DEFAULT_RULES = new ApplyFirst<>(new SurviveRule(), new UnderpopulationRule(), new OverpopulationRule(), new ReproductionRule());

    /**
     * Returns true if this state represents a living cell.
     *
     * @return true if this state represents a living cell.
     */
    public boolean isAlive() {
        return this==LIVE;
    }

    /**
     * Returns true if this state represent a dead cell.
     *
     * @return true if this state represent a dead cell.
     */
    public boolean isDead() {
        return this==DEAD;
    }

    public ConwayState swap() {
        return switch (this) {
            case DEAD -> LIVE;
            case LIVE -> DEAD;
        };
    }
}
