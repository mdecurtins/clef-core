package clef.mir.dataset;

import java.util.List;

import clef.common.ClefException;

/**
 * Simple interface for dataset operations.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public interface DatasetService {

	
	/**
	 * Gets a list of all datasets stored by the DatasetService.
	 * 
	 * @since 1.0.0
	 * @return a list of all datasets available, empty if none found.
	 */
	public List<Dataset> getAllDatasets();
	
	
	/**
	 * Gets a dataset by name as specified in its clefdataset.json file. 
	 * 
	 * @since 1.0.0
	 * @param name the name of the desired dataset, as specified in its clefdataset.json file
	 * @return the Dataset if successful, null if not found
	 * @throws ClefException
	 */
	public Dataset getDataset( String name ) throws ClefException;
	

	
	/**
	 * Lists the names of all available datasets.
	 * 
	 * @since 1.0.0
	 * @return a list of dataset names, empty if none found.
	 */
	public List<String> listDatasetNames();
	
}
