package it.unicam.cs.pa.jgol.model;

import it.unicam.cs.pa.jgol.io.LocationReader;
import it.unicam.cs.pa.jgol.io.LocationWriter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GridCoordinates implements Location<GridCoordinates> {

    public static final LocationWriter<GridCoordinates> WRITER = GridCoordinates::stringOf;
    public static final LocationReader<GridCoordinates> LOADER = GridCoordinates::fromString;

    public static String stringOf(GridCoordinates g) {
        return String.format("%d %d", g.row, g.column);
    }

    public static GridCoordinates fromString(String str) throws IOException {
        String[] elements = str.split(" ");
        if (elements.length == 2) {
            try {
                int row = Integer.parseInt(elements[0]);
                int column = Integer.parseInt(elements[1]);
                return new GridCoordinates(row, column);
            } catch (RuntimeException e) {
            }
        }
        throw new IOException("Illegal format! Expected \"<num> <num>\" is "+str);
    }


    private final int row;
    private final int column;

    public GridCoordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public List<GridCoordinates> neighbours() {
        return List.of(
                new GridCoordinates(getRow()+1, getColumn()+1),
                new GridCoordinates(getRow()+1, getColumn()),
                new GridCoordinates(getRow()+1, getColumn()-1),
                new GridCoordinates(getRow(), getColumn()+1),
                new GridCoordinates(getRow(), getColumn()-1),
                new GridCoordinates(getRow()-1, getColumn()+1),
                new GridCoordinates(getRow()-1, getColumn()),
                new GridCoordinates(getRow()-1, getColumn()-1)
        );
    }


    public GridCoordinates getCenter(List<GridCoordinates> list) {
        if (list.isEmpty()) {
            return new GridCoordinates(0,0);
        }
        int row = list.stream().mapToInt(GridCoordinates::getRow).sum()/list.size();
        int column = list.stream().mapToInt(GridCoordinates::getColumn).sum()/list.size();
        return new GridCoordinates(row, column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridCoordinates that = (GridCoordinates) o;
        return getRow() == that.getRow() && getColumn() == that.getColumn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn());
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}
