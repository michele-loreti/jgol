package it.unicam.cs.pa.jgol.model;

import java.util.List;

public class Grid implements Space<GridCoordinates>{
    @Override
    public List<GridCoordinates> neighbours(GridCoordinates gridCoordinates) {
        return gridCoordinates.neighbours();
    }
}
