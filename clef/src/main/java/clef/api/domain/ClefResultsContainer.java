package clef.api.domain;

import java.util.List;

public class ClefResultsContainer implements ClefResponseBody {

	private List<ClefItem> items;
	
	@Override
	public List<ClefItem> getItems() {
		return this.items;
	}
	
	
	
	public void setAllItems( List<ClefItem> items ) {
		this.items = items;
	}

}
