package clef;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Ignore;

import java.io.IOException;
import java.nio.file.Path;

import java.util.List;

import clef.common.ClefException;
import clef.mir.AlgorithmEnvironment;
import clef.mir.AlgorithmEnvironmentService;
import clef.mir.AlgorithmEnvironmentServiceImpl;

public class AlgorithmEnvironmentServiceTest {

	@Ignore
	@Test
	public void testFindClefinfo() {
		
		try {
			AlgorithmEnvironmentService aes = AlgorithmEnvironmentServiceImpl.getInstance();
			List<Path> found = aes.getDefinedAlgorithms();

			Assert.assertEquals( 1, found.size() );
			System.out.println( "Found: " + found.get(0).toString() );
		} catch ( ClefException | IOException ce ) {
			ce.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void testMapAlgorithmEnvironment() {

		
		try {
			AlgorithmEnvironmentService aes = AlgorithmEnvironmentServiceImpl.getInstance();
			List<Path> found = aes.getDefinedAlgorithms();
			
			try {
				AlgorithmEnvironment ae = AlgorithmEnvironment.fromFile( found.get(0) );
				
				Assert.assertNotNull( ae );

			} catch ( IOException ioe ) {
				ioe.printStackTrace();
			}
		} catch ( ClefException | IOException ce ) {
			ce.printStackTrace();
		}
	}
}
