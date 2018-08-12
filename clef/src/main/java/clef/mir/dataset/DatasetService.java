package clef.mir.dataset;

import java.nio.file.Path;
import java.util.List;

import clef.common.ClefException;

/**
 * 
 * @author Max DeCurtins
 *
 */
public interface DatasetService {

	/**
	 * 
	 * @return
	 */
	public List<Dataset> getAllDatasets();
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws ClefException
	 */
	public Dataset getDataset( String name ) throws ClefException;
	

	
	/**
	 * 
	 * @return
	 */
	public List<String> listDatasetNames();
	
}
