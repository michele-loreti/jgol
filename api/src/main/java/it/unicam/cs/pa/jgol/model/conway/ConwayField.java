package it.unicam.cs.pa.jgol.model.conway;

import it.unicam.cs.pa.jgol.model.Environment;
import it.unicam.cs.pa.jgol.model.Location;
import it.unicam.cs.pa.jgol.model.Rule;
import it.unicam.cs.pa.jgol.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * This class represents the ConwayField.
 */
public class ConwayField<L extends Location<L>> implements Environment<ConwayState, L> {

    /**
     * This set is used to store cells that are alive.
     */
    private final Set<L> aliveCells;

    /**
     * Creates an empty field for the Conway model.
     */
    public ConwayField() {
        this(new HashSet<>());
    }

    /**
     * Creates a field for the Conway model with the given cells.
     *
     * @param aliveCells the set of cells that are alive.
     */
    protected ConwayField(Set<L> aliveCells) {
        this.aliveCells = aliveCells;
    }

    /**
     * A copy constructor.
     *
     * @param conwayField field to copy.
     */
    private ConwayField(ConwayField<L> conwayField) {
        this(new HashSet<>(conwayField.aliveCells));
    }

    @Override
    public ConwayState statusOf(L loc) {
        return (this.aliveCells.contains(loc)?ConwayState.LIVE: ConwayState.DEAD);
    }

    @Override
    public Map<L, ConwayState> getStatusMapOf(Collection<L> set) {
        return set.stream().collect(Collectors.toMap(l -> l, this::statusOf));
    }

    @Override
    public List<ConwayState> statusOf(Collection<L> locs) {
        return locs.stream().map(this::statusOf).collect(Collectors.toList());
    }

    @Override
    public Map<L, ConwayState> evolvingCells(Rule<ConwayState> rule) {
        return Stream.concat( //Create of involved locations
                    this.aliveCells.stream(), //Locations that are alive
                    this.aliveCells.stream().map(Location::neighbours).flatMap(List::stream) //Locations next to an alive cell
                ).distinct()//Remove duplicates
                .map(l -> new Pair<>(l, rule.apply(l, this::statusOf)))//Apply the rule and store the result in a pair
                .filter(p -> p.testSecond(Optional::isPresent))//Remove elements where the rule has not been applied
                .map(p -> p.map(Optional::get)) //Get new states
                .filter(p -> p.test((l,s) -> statusOf(l) != s)) //Remove locations that have not changed their state
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)); //Collect in a map
    }

    @Override
    public Environment<ConwayState, L> set(Map<L, ConwayState> updates) {
        ConwayField<L> newField = new ConwayField<>(this);
        updates.forEach(newField::set);
        return newField;
    }

    @Override
    public List<L> getLocations(ConwayState state) {
        return List.copyOf(aliveCells);
    }

    @Override
    public void set(L loc, ConwayState state) {
        if (state.isAlive()) {
            setAlive(loc);
        } else {
            setDead(loc);
        }
    }

    /**
     * Sets the cell at the given location as alive.
     *
     * @param loc a location.
     */
    public void setAlive(L loc) {
        this.aliveCells.add(loc);
    }

    /**
     * Sets the cell at the given location as dead.
     *
     * @param loc a location.
     */
    public void setDead(L loc) {
        this.aliveCells.remove(loc);
    }

    public List<L> getAliveCells() {
        return this.aliveCells.stream().toList();
    }
}
