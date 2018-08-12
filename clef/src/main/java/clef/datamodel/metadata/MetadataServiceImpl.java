package clef.datamodel.metadata;

import clef.api.domain.ClefResult;
import clef.datamodel.Composer;
import clef.datamodel.Era;
import clef.datamodel.Single;
import clef.datamodel.Tag;
import clef.datamodel.TagRelation;
import clef.datamodel.WorkType;
import clef.datamodel.db.*;
import clef.mir.AlgorithmEnvironmentResponse;
import clef.mir.dataset.Dataset;

import java.util.List;

public class MetadataServiceImpl {

	public List<ClefResult> mapMetadata( AlgorithmEnvironmentResponse aer ) {
		
		
		
		return null;
	}
	
	public int insertComposer( Composer c ) {

		return 0;
	}
	
	public int insertEra( Era era ) {
		return this.insertSingle( era );
	}
	
	public int insertTag( Tag tag ) {
		return this.insertSingle( tag );
	}
	
	public int insertTagRelation( TagRelation tr ) {
		InsertRelation ir = new InsertRelation( tr );
		Database db = new Database();
		return db.insert( ir );
	}
	
	public int insertWorkType( WorkType wt ) {
		return this.insertSingle( wt );
	}
	
	private <T extends Single> int insertSingle( T t ) {
		InsertSingle ins = new InsertSingle( t );
		Database db = new Database();
		return db.insert( ins );
	}
	
	public void populateMetadata( Dataset dset, List<Metadata> dsetFiles ) {
		// Iterate through files
		// Parse metadata
		// Store in database
	}
}
