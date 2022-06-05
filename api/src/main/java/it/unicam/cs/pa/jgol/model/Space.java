package it.unicam.cs.pa.jgol.model;

import java.util.List;

public interface Space<C> {

    List<C> neighbours(C c);

}
