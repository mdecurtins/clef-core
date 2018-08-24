package clef.utility;

import java.io.IOException;

/**
 * Functional interface declaring that the apply() method may throw an IOException.
 * 
 * @author Max DeCurtins
 *
 * @param <T>
 * @param <R>
 * 
 * @since 1.0.0
 */
@FunctionalInterface
public interface CheckedFunction<T, R> {
	R apply( T t ) throws IOException;
}
