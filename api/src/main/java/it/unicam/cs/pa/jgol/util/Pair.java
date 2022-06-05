package it.unicam.cs.pa.jgol.util;

import it.unicam.cs.pa.jgol.model.GridCoordinates;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a pair of elements.
 *
 * @param <T> type of first element in the pair.
 * @param <R> type of second element in the pair.
 */
public final class Pair<T,R> {

    private final T first;
    private final R second;

    /**
     * Creates a new pair.
     *
     * @param first first element in the pair.
     * @param second second element in the pair.
     */
    public Pair(T first, R second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first element in this pair.
     *
     * @return  the first element in this pair.
     */
    public T getFirst() {
        return first;
    }

    /**
     * Returns the second element in this pair.
     *
     * @return the second element in this pair.
     */
    public R getSecond() {
        return second;
    }

    /**
     * Returns true if the elements of this pair satisfy the given predicate.
     *
     * @param predicate a predicate.
     * @return true if the elements of this pair satisfy the given predicate.
     */
    public boolean test(BiPredicate<? super T, ? super R> predicate) {
        return predicate.test(first, second);
    }


    /**
     * Returns true if the first element of this pair satisfies the given predicate.
     *
     * @param predicate a predicate.
     * @return true if the first element of this pair satisfies the given predicate.
     */
    public boolean testFirst(Predicate<? super T> predicate) {
        return predicate.test(first);
    }

    /**
     * Returns true if the second element of this pair satisfies the given predicate.
     *
     * @param predicate a predicate.
     * @return true if the second element of this pair satisfies the given predicate.
     */
    public boolean testSecond(Predicate<? super R> predicate) {
        return predicate.test(second);
    }


    /**
     * Returns the pair <code>(a,b)</code> such that <code>a = this.getFirst()</code>  and <code>b = f.apply(second) </code>.
     *
     * @param f a function
     * @return the pair obtained from this one by applying to the second element the given function.
     * @param <S> type of the second element in the created pair.
     */
    public <S> Pair<T,S> map(Function<? super R,? extends S> f) {
        return new Pair<>(first, f.apply(second));
    }

    /**
     * Returns the pair <code>(a,b)</code> such that <code>a = this.getFirst()</code>  and <code>b = f.apply(this.getFirst(), this.getSecond())</code>.
     *
     * @param f a function
     * @return the pair obtained from this one by applying to the second element the given function.
     * @param <S> type of the second element in the created pair.
     */
    public <S> Pair<T,S> map(BiFunction<? super T, ? super R, ? extends S> f) {
        return new Pair<>(first, f.apply(first, second));
    }

}
