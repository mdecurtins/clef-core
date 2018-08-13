package clef.datamodel.metadata;

import clef.api.domain.ClefResult;
import clef.datamodel.*;
import clef.datamodel.db.*;
import clef.datamodel.metadata.parsers.Humdrum;
import clef.mir.AlgorithmEnvironmentResponse;
import clef.mir.MusicFormat;
import clef.mir.dataset.Dataset;
import clef.utility.CheckedFunction;
import clef.utility.ClefUtility;
import clef.utility.FileHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MetadataServiceImpl {

	public List<ClefResult> mapMetadata( AlgorithmEnvironmentResponse aer ) {
		
		
		
		return null;
	}
	
	/**
	 * 
	 * @param mf
	 * @return
	 */
	private CheckedFunction<Path, Metadata> createFunctionForFormat( MusicFormat mf ) {
		CheckedFunction<Path, Metadata> callback = null;
		switch ( mf ) {
		case HUMDRUM:
			callback = Humdrum.getCreatorFunction();
			break;
		default:
			callback = null;
		}
		return callback;
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
	
	
	/**
	 * 
	 * @param dsets
	 * @throws IOException
	 */
	public void populateMetadata( List<Dataset> dsets ) throws IOException {
		
		for ( Dataset d : dsets ) {
			
			MusicFormat mf = MusicFormat.valueOf( d.getDatasetAttributes().getFormat() );
			
			Predicate<Path> predicate = this.predicateForFormat( mf );
			CheckedFunction<Path, Metadata> creatorFunction = this.createFunctionForFormat( mf );
			
			List<Metadata> meta = new ArrayList<Metadata>();
			// Start gathering metadata from files in the directory where the clefdataset.json file resides.
			if ( ! d.getParentDirectory().equals( "" ) ) {
				meta = FileHandler.traversePath( d.getParentDirectory(), predicate, creatorFunction );
			}
			
			if ( ! meta.isEmpty() ) {
				// Loop over the Metadata instances and populate with information.
				for ( Metadata m : meta ) {
					
					// Update the dataset content record with the collection name and dataset name.
					DatasetContent dc = m.getDatasetContent();
					dc.setCollection( d.getCollection() );
					dc.setDatasetName( d.getDatasetAttributes().getName() );
					m.setDatasetContent( dc );
					
					
				}
			}
		}
		
	}
	
	private void insertEras( List<Metadata> meta ) {
		
		// Get Metadata instances with unique values for Era
		List<Metadata> uniqueByEra = meta.stream().filter( ClefUtility.distinctByKey( Metadata::getEra ) ).collect( Collectors.toList() );
		
		if ( ! uniqueByEra.isEmpty() ) {
			List<Era> uniqueEras = new ArrayList<Era>();
			for ( Metadata m : uniqueByEra ) {
				uniqueEras.add( m.getEra() );
			}
			
			if ( ! uniqueEras.isEmpty() ) {
				for ( Era era : uniqueEras ) {
					int eraID = this.insertEra( era );
					era.setId( eraID );
				}
			}
		}
	}
	
	/**
	 * 
	 * @param mf
	 * @return
	 */
	private Predicate<Path> predicateForFormat( MusicFormat mf ) {
		Predicate<Path> pred = null;
		switch ( mf ) {
		case HUMDRUM:
			pred = Humdrum.getPredicate();
			break;
		default:
			pred = null;
		}
		return pred;
	}
}
