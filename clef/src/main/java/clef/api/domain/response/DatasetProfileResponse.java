package clef.api.domain.response;

import java.util.List;
import java.util.ArrayList;

import clef.mir.dataset.Dataset;

/**
 * Class representing an API response containing information about symbolic music datasets.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class DatasetProfileResponse extends ClefResponse {

	private List<Dataset> datasets;
	
	public DatasetProfileResponse() {
		super();
		this.responseType = ResponseType.DATASET_PROFILE.toString();
		this.datasets = new ArrayList<Dataset>();
	}
	
	public List<Dataset> getDatasets() {
		return datasets;
	}
	
	public <T> void setData( List<T> l ) {
		for ( T o : l ) {
			Dataset dset = (Dataset) o;
			if ( ! this.datasets.contains( dset ) ) {
				this.datasets.add( dset );
			}
		}
	}
}
