package clef.api.domain;

import java.util.List;

public interface ClefResponseBody {

	public List<ClefItem> getItems();
	
	public void setAllItems( List<ClefItem> items );
}
