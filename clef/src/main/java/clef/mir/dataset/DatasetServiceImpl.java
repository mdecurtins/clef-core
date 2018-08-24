package clef.mir.dataset;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import clef.common.ClefException;
import clef.utility.FileHandler;

/**
 * Class that provides access to datasets.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class DatasetServiceImpl implements DatasetService {

	private Map<String, Dataset> datasets;
	
	private static DatasetService instance;

	
	private DatasetServiceImpl() throws ClefException, IOException {
		this.datasets = new HashMap<String, Dataset>();
		
		List<Dataset> dsets = Dataset.discoverDatasets();
		
		if ( dsets.isEmpty() ) {
			throw new ClefException( "DatasetService: no clefdataset.json files within the datasets directory tree." );
		}
		
		for ( Dataset dset : dsets ) {
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
