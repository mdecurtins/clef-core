package clef.api.domain.response;

import java.util.List;

public class DatasetProfileResponse extends ClefResponse {

	public DatasetProfileResponse() {
		super();
		this.responseType = ResponseType.DATASET_PROFILE.toString();
	}
	
	public <T> void setData( List<T> l ) {
		
	}
}
