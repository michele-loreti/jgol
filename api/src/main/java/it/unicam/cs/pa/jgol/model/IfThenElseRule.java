package it.unicam.cs.pa.jgol.model;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

public class IfThenElseRule<S> implements Rule<S> {

    private final BiPredicate<S, List<S>> guard;
    private final Rule<S> thenRule;
    private final Rule<S> elseRule;

    public IfThenElseRule(BiPredicate<S, List<S>> guard, Rule<S> thenRule, Rule<S> elseRule) {
        this.guard = guard;
        this.thenRule = thenRule;
        this.elseRule = elseRule;
    }

    @Override
    public Optional<S> apply(S cellStatus, List<S> neighboursStatus) {
        if (guard.test(cellStatus, neighboursStatus)) {
            return thenRule.apply(cellStatus, neighboursStatus);
        } else {
            return elseRule.apply(cellStatus, neighboursStatus);
        }
    }
}
