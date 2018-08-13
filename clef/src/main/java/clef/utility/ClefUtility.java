package clef.utility;

import java.util.function.Function;
import java.util.function.Predicate;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClefUtility {

	/**
	 * Utility method to return a predicate that filters a stream by distinct values obtained from getters.
	 * 
	 * @since 1.0.0
	 * @see https://stackoverflow.com/questions/23699371/java-8-distinct-by-property
	 * @param getter the getter for the property whose uniqueness is to be evaluated
	 * @return
	 */
	public static <T> Predicate<T> distinctByKey( Function<? super T, ?> getter ) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add( getter.apply( t ) );
	}
}
