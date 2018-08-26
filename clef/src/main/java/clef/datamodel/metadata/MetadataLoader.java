package clef.datamodel.metadata;


import clef.datamodel.*;
import clef.datamodel.db.*;
import clef.datamodel.metadata.parsers.Humdrum;
import clef.mir.MusicFormat;
import clef.mir.dataset.Dataset;
import clef.utility.CheckedBiFunction;
import clef.utility.FileHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Class to scan for, parse, and load metadata for symbolic music datasets.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
@Component
public class MetadataLoader implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger( MetadataLoader.class );
	
	
	/**
	 * Gets the creator function for the given symbolic music format.
	 *  
	 * @param mf the symbolic music format.
	 * @return a function that will create a Metadata instance given a path and a dataset.
	 * @since 1.0.0
	 */
	private CheckedBiFunction<Path, Dataset, Metadata> createFunctionForFormat( MusicFormat mf ) {
		CheckedBiFunction<Path, Dataset, Metadata> callback = null;
		switch ( mf ) {
		case HUMDRUM:
			callback = Humdrum.getCreatorFunction();
			break;
		default:
			callback = null;
		}
		return callback;
	}

	
	/**
	 * Performs a series of foreign-key dependent inserts.
	 * 
	 * N.B. Every insert*() call inside this method mutates the {@code List<Metadata>}!
	 * 
	 * 
	 * @param meta a list of {@link Metadata} instances created from parsed symbolic music source files
	 * @since 1.0.0
	 */
	private void doInserts( List<Metadata> meta ) {
		logger.info( "Performing inserts..." );
		
		// 1. Populate tables with no foreign keys: composers, dataset_contents, eras, tags, work_types
		this.insertEras( meta );
		this.insertWorkTypes( meta );
		this.insertComposers( meta );
		this.insertTags( meta );
		this.insertDatasetContents( meta );
		
		// 2. Populate tables with foreign keys dependent on composers, dataset_contents, eras, tags, and work_types: works
		this.insertWorks( meta );
		
		// 3. Populate tables with foreign keys dependent on works: tag_relations
		this.insertTagRelations( meta );
	}
	
	
	/**
	 * Inserts composer metadata into the database.
	 * 
	 * Note that the process involves functional filtering of the incoming {@code List<Metadata>} and then 
	 * updating, via a matching predicate, the items in that same List with new instances of the Composer object.
	 * 
	 * The result is a {@code List<Metadata>} that is modified in place. 
	 * 
	 * @param meta
	 * @since 1.0.0
	 */
	private void insertComposers( List<Metadata> meta ) {
	
		ComposerDAO cdao = new ComposerDAO();
		// Map composers from the list of Metadata instances.
		List<Composer> comps = cdao.mapFromMetadata( meta );
		
		// Insert the composers into the database.
		cdao.batchInsert( comps );

		// Select all composers from the database.
		List<Composer> allComposers = cdao.selectAll();

		// Update the list of Metadata.
		if ( ! allComposers.isEmpty() ) {
			cdao.updateMetadata( meta, allComposers );
		}
		
	}
	
	
	/**
	 * Inserts records of each file in a dataset into the database.
	 * 
	 * Note that the process involves functional filtering of the incoming {@code List<Metadata>} and then 
	 * updating, via a matching predicate, the items in that same List with new instances of the DatasetContent object.
	 * 
	 * The result is a {@code List<Metadata>} that is modified in place.
	 * 
	 * @param meta
	 * @since 1.0.0
	 */
	private void insertDatasetContents( List<Metadata> meta ) {

		DatasetContentDAO dcdao = new DatasetContentDAO();
		// Map from metadata.
		List<DatasetContent> dsetCont = dcdao.mapFromMetadata( meta );
		
		// Insert into the database.
		dcdao.batchInsert( dsetCont );
		
		// Select all dataset contents.
		List<DatasetContent> allDsetCont = dcdao.selectAll();
		if ( ! allDsetCont.isEmpty() ) {
			dcdao.updateMetadata( meta, allDsetCont );
		}
	}
	
	
	/**
	 * Inserts Era values into the database and updates Metadata instances with the resulting IDs.
	 * 
	 * Note that the process involves functional filtering of the incoming {@code List<Metadata>} and then 
	 * updating, via a matching predicate, the items in that same List with new instances of the Era object.
	 * 
	 * The result is a {@code List<Metadata>} that is modified in place.
	 * 
	 * @param meta
	 * @see EraDAO#updateMetadata(List, List)
	 * @since 1.0.0
	 */
	private void insertEras( List<Metadata> meta ) {

		EraDAO ed = new EraDAO();
		// Extract the Era instances from the list of Metadata instances with unique values for Era
		List<Era> eras = ed.mapFromMetadata( meta );
		
		// Insert into the database.
		ed.batchInsert( eras );
	
		// Select all from the database.
		List<Era> allEras = ed.selectAll();
		
		// Update the entire list of metadata, matching on era value.
		if ( ! allEras.isEmpty() ) {
			ed.updateMetadata( meta, allEras );
		}
		
	}
	
	
	/**
	 * Inserts tags into the database.
	 * 
	 * Note that the process involves functional filtering of the incoming {@code List<Metadata>} and then 
	 * updating, via a matching predicate, the items in that same List with new instances of the Tag object.
	 * 
	 * The result is a {@code List<Metadata>} that is modified in place.
	 * 
	 * @param meta
	 * @since 1.0.0
	 */
	private void insertTags( List<Metadata> meta ) {
		
		TagDAO tdao = new TagDAO();
		// Map from metadata
		List<Tag> tags = tdao.mapFromMetadata( meta );
		
		// Insert into the database
		tdao.batchInsert( tags );
		
		// Select all and update the metadata
		List<Tag> allTags = tdao.selectAll();
		if ( ! allTags.isEmpty() ) {
			tdao.updateMetadata( meta, allTags );
		}
		
	}
	
	
	/**
	 * Inserts tag-work relational records into the database.
	 * 
	 * @param meta
	 * @since 1.0.0
	 */
	private void insertTagRelations( List<Metadata> meta ) {
		
		// This method requires no mapping or selection, because we already have the info we need.
		TagRelationDAO trdao = new TagRelationDAO();
		trdao.batchInsert( meta );
		
	}
	
	
	/**
	 * Inserts work type records into the database.
	 * 
	 * Note that the process involves functional filtering of the incoming {@code List<Metadata>} and then 
	 * updating, via a matching predicate, the items in that same List with new instances of the WorkType object.
	 * 
	 * The result is a {@code List<Metadata>} that is modified in place.
	 * 
	 * @param meta
	 * @since 1.0.0
	 */
	private void insertWorkTypes( List<Metadata> meta ) {
		
		WorkTypeDAO wtd = new WorkTypeDAO();
		// Map from metadata.
		List<WorkType> wts = wtd.mapFromMetadata( meta );
		
		// Insert into the database.
		wtd.batchInsert( wts );
		
		// Select all and use to update the Metadata.
		List<WorkType> allWorkTypes = wtd.selectAll();
		if ( ! allWorkTypes.isEmpty() ) {	
			wtd.updateMetadata( meta, allWorkTypes );
		}
		
	}
	
	
	/**
	 * Inserts musical works into the database.
	 * 
	 * Note that the process involves functional filtering of the incoming {@code List<Metadata>} and then 
	 * updating, via a matching predicate, the items in that same List with new instances of the Work object.
	 * 
	 * The result is a {@code List<Metadata>} that is modified in place.
	 * 
	 * @param meta
	 * @since 1.0.0
	 */
	private void insertWorks( List<Metadata> meta ) {

		WorkDAO wd = new WorkDAO();
		
		// Map from metadata.
		List<Work> works = wd.mapFromMetadata( meta );
		
		// Insert into the database.
		wd.batchInsert( works );

		// Select all and update metadata.
		List<Work> allWorks = wd.selectAll();
		if ( ! allWorks.isEmpty() ) {
			wd.updateMetadata( meta, allWorks );
		}
	}
	
	
	/**
	 * Parses metadata from files and inserts it into the database. 
	 * 
	 * @param dsets
	 * @throws IOException
	 * @see {@link clef.mir.dataset.Dataset#discoverDatasets()}
	 * @see {@link clef.utility.FileHandler#traversePath(String, Predicate, clef.common.ClefDiscoverable, CheckedBiFunction)}
	 * @since 1.0.0
	 */
	public void populateMetadata( List<Dataset> dsets ) throws IOException {
		
		logger.info( "Populating metadata..." );
		
		for ( Dataset d : dsets ) {
			
			// What type of symbolic music files should we look for?
			MusicFormat mf = MusicFormat.valueOf( d.getDatasetAttributes().getFormat().toUpperCase() );
			
			// Which predicate and creator functions should be used for instantiating Metadata instances?
			// Predicate determines what files should be scanned for metadata. Creator function reads those files and returns Metadata objects.
			Predicate<Path> predicate = this.predicateForFormat( mf );
			CheckedBiFunction<Path, Dataset, Metadata> creatorFunction = this.createFunctionForFormat( mf );
			
			List<Metadata> meta = new ArrayList<Metadata>();
			
			// Start gathering metadata from files in the directory where the clefdataset.json file resides.
			if ( ! d.getParentDirectory().equals( "" ) ) {
				logger.info( "Traversing files..." );
				meta = FileHandler.traversePath( d.getParentDirectory(), predicate, d, creatorFunction );
			} else {
				logger.error( "populateMetadata: parent directory was an empty string." );
			}
			
			if ( ! meta.isEmpty() ) {
				// Insert the data in stages.
				this.doInserts( meta );			
			} else {
				logger.error( "populateMetadata: list of Metadata was empty.");
			}
		}
		
	}
	
	
	/**
	 * Gets a file-matching predicate for the given symbolic music format.
	 * 
	 * @param mf
	 * @return a predicate used to determine which files should be parsed for metadata.
	 * @since 1.0.0
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


	@Override
	public void run(String... args) throws Exception {
		
		logger.info( "Invoking MetadataLoader...");

		List<Dataset> dsets = new ArrayList<Dataset>();
		try {
			logger.info( "Discovering datasets...");
			dsets = Dataset.discoverDatasets();
			
			this.populateMetadata( dsets );
			
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
		
	}
}
