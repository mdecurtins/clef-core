package clef.api.domain;

import java.util.ArrayList;
import java.util.List;

public class ClefErrorsContainer implements ClefResponseBody {

	private List<ClefItem> items;
	
	public ClefErrorsContainer() {
		this.items = new ArrayList<ClefItem>();
	}
	
	public boolean addItem( ClefItem item ) {
		return this.items.add( item );
	}
	
	
	public List<ClefItem> getItems() {
		return this.items;
	}

	public void setAllItems( List<ClefItem> items ) {
		this.items = items;
	}
}
