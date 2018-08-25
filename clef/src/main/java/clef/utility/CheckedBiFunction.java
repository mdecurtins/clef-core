package clef.utility;

import java.io.IOException;

/**
 * Functional interface declaring that the apply() method may throw an IOException.
 * 
 * @author Max DeCurtins
 *
 * @param <T>
 * @param <U>
 * @param <R>
 * 
 * @since 1.0.0
 */
@FunctionalInterface
public interface CheckedBiFunction<T, U, R> {

	R apply( T t, U u ) throws IOException;
	
}
