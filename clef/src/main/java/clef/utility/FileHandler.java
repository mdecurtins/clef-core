package clef.utility;

import java.io.IOException;

import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.List;
import java.util.function.Predicate;
import java.util.ArrayList;

/**
 * Class to handle various file-related operations.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class FileHandler {

	/**
	 * Method to return a list of objects created from files in a given path.
	 * 
	 * @since 1.0.0
	 * @param root the path from which to start traversing the file tree
	 * @param pred the predicate that should be used to test files for this call
	 * @param createFunc the function that creates an object instance, given a path
	 * @return
	 * @throws IOException
	 */
	public static <T> List<T> traversePath( String root, Predicate<Path> pred, CheckedFunction<Path, T> createFunc ) throws IOException {
		
		List<T> l = new ArrayList<T>();
		
		// Start file tree traversal at the given root.
		Path p = FileSystems.getDefault().getPath( root );
		
		Files.walkFileTree( p, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException {
				
				// Test the predicate passed to see if the current file path is one relevant to this method invocation.
				if ( pred.test( file ) ) {
					
					// If so, execute the creator function callback passed, which will return a concrete object instance based on the current path.
					l.add( createFunc.apply( file ) );
				}
				
				return FileVisitResult.CONTINUE;
			}
		});
		
		// Return the list.
		return l;
	}
	
	
}
