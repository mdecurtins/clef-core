package clef.mir.dataset;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import clef.common.ClefException;

public class DatasetServiceImpl implements DatasetService {

	private Map<String, Dataset> datasets;
	private static final String ENV_DATASETS_DIR = "DATASETS_DIR";
	private static DatasetService instance;
	
	
	private DatasetServiceImpl() throws ClefException, IOException {
		this.datasets = new HashMap<String, Dataset>();
		
		List<Path> datasetPaths = this.getDefinedDatasetPaths();
		
		if ( datasetPaths.isEmpty() ) {
			throw new ClefException( "DatasetService: no clefdataset.json files within the datasets directory tree." );
		}
		
		for ( Path path : datasetPaths ) {
			Dataset dset = Dataset.fromFile( path );
			if ( dset instanceof Dataset ) {
				this.datasets.put( dset.getDatasetAttributes().getName(), dset );
			} else {
				System.err.println( "Dataset.fromFile() did not produce an instance of Dataset." );
			}
		}
		
	}
	
	
	public List<Dataset> getAllDatasets() {
		List<Dataset> dsets = new ArrayList<Dataset>();
		for ( Dataset d : this.datasets.values() ) {
			dsets.add( d );
		}
		return dsets;
	}
	
	
	public Dataset getDataset( String name ) throws ClefException {
		Dataset dset = this.datasets.get( name );
		if ( dset == null ) {
			throw new ClefException( "DatasetService did not find a dataset with name: " + name );
		}
		return dset;
	}
	
	
	public List<Path> getDefinedDatasetPaths() throws ClefException {
		List<Path> clefdatasetPaths = new ArrayList<Path>();
		
		String datasetsPath = System.getenv( ENV_DATASETS_DIR );
		if ( datasetsPath == null ) {
			throw new ClefException( "Environment variable " + ENV_DATASETS_DIR + " is not defined or could not be retrieved." );
		}
		
		Path p = FileSystems.getDefault().getPath( datasetsPath );
		
		try {
			Files.walkFileTree( p, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException {
					String absoluteFilename = file.toAbsolutePath().toString();
					if ( absoluteFilename.endsWith( "clefdataset.json" ) ) {
						clefdatasetPaths.add( file.toAbsolutePath() );
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
		
		return clefdatasetPaths;
	}
	
	public static DatasetService getInstance() throws ClefException, IOException {
		if ( instance == null ) {
			instance = new DatasetServiceImpl();
		}
		return instance;
	}
	
	
	public List<String> listDatasetNames() {
		List<String> dsetNames = new ArrayList<String>();
		for ( String name : this.datasets.keySet() ) {
			dsetNames.add( name );
		}
		return dsetNames;
	}
}
