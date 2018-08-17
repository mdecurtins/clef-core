package clef.datamodel.db;

import java.util.List;
import java.util.ArrayList;
import java.util.function.BiPredicate;
import java.util.function.BiConsumer;

import clef.datamodel.*;
import clef.datamodel.metadata.Metadata;

public class DatasetContentDAO extends ClefDAO {

	private static BiPredicate<DatasetContent, Metadata> matchDatasetContent = ( dc, m ) -> dc.getDatasetName().equals( m.getDatasetContent().getDatasetName() ) &&
			dc.getFilename().equals( m.getDatasetContent().getFilename() );
	
	public int batchInsert( List<DatasetContent> dsetCont ) {
		String sql = "INSERT INTO dataset_contents VALUES ( ?, ?, ?, ?, ? ) ON DUPLICATE KEY UPDATE id = id;";
		return 0;
	}
	
	public static BiPredicate<DatasetContent, Metadata> getMatchingPredicate() {
		return matchDatasetContent;
	}
	
	public List<DatasetContent> mapFromMetadata( List<Metadata> meta ) {
		List<DatasetContent> dsetConts = new ArrayList<DatasetContent>();
		
		return dsetConts;
	}
	
	public List<DatasetContent> selectAll() {
		List<DatasetContent> dsetConts = new ArrayList<DatasetContent>();
		
		return dsetConts;
	}
	
	public void updateMetadata( List<Metadata> meta, List<DatasetContent> dsetCont ) {
		
	}
	
}
