package clef.utility;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import java.util.List;

public class FileHandler {

	public static enum VisitWhat {
		FILES, DIRS
	}
	
	public static void traversePath( String root, String matchWhat, VisitWhat visitWhat ) {
		
	}
	
	public static <T> void traversePath( String root, String matchWhat, List<T> l, VisitWhat visitWhat ) {
		Path p = FileSystems.getDefault().getPath( root );
	}
}
