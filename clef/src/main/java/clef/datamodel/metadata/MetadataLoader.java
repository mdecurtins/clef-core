package clef.datamodel.metadata;


import clef.datamodel.*;
import clef.datamodel.db.*;
import clef.datamodel.metadata.parsers.Humdrum;
import clef.mir.MusicFormat;
import clef.mir.dataset.Dataset;
import clef.utility.CheckedBiFunction;
import clef.utility.CheckedFunction;
import clef.utility.ClefUtility;
import clef.utility.FileHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MetadataLoader implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger( MetadataLoader.class );
	/**
	 * 
	 * @param mf
	 * @return
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
	 * @param meta
	 * @since 1.0.0
	 */
	private void doInserts( List<Metadata> meta ) {
		logger.info( "Calling doInserts()..." );
		
		// 1. Populate tables with no foreign keys: composers, eras, tags, work_types
		this.insertEras( meta );
		this.insertWorkTypes( meta );
		this.insertComposers( meta );
		this.insertTags( meta );
		
		// 2. Populate tables with foreign keys dependent on composers, eras, tags, and work_types: works
		this.insertWorks( meta );
		
		// 3. Populate tables with foreign keys dependent on works: dataset_contents and tag_relations
	}
	
	
	/**
	 * 
	 * @param dsets
	 * @throws IOException
	 * @since 1.0.0
	 */
	public void populateMetadata( List<Dataset> dsets ) throws IOException {
		
		logger.info( "Calling populateMetadata()...");
		
		if ( dsets.isEmpty() ) {
			System.out.println( "MetadataService: received empty list of Datasets.");
		}
		
		for ( Dataset d : dsets ) {
			
			logger.info( "Dataset name is: " + d.getDatasetAttributes().getName() + " format is: " + d.getDatasetAttributes().getFormat() );
			// What type of symbolic music files should we look for?
			MusicFormat mf = MusicFormat.valueOf( d.getDatasetAttributes().getFormat().toUpperCase() );
			
			// Which predicate and creator functions should be used for instantiating Metadata instances?
			Predicate<Path> predicate = this.predicateForFormat( mf );
			CheckedBiFunction<Path, Dataset, Metadata> creatorFunction = this.createFunctionForFormat( mf );
			
			List<Metadata> meta = new ArrayList<Metadata>();
			// Start gathering metadata from files in the directory where the clefdataset.json file resides.
			if ( ! d.getParentDirectory().equals( "" ) ) {
				logger.info( "Parent directory is: " + d.getParentDirectory() );
				meta = FileHandler.traversePath( d.getParentDirectory(), predicate, d, creatorFunction );
			} else {
				System.out.println( "populateMetadata: parent directory was an empty string." );
			}
			
			if ( ! meta.isEmpty() ) {
				
				this.doInserts( meta );
				/*
				// Loop over the Metadata instances and populate with information
				for ( Metadata m : meta ) {
					
					// Update the dataset content record with the collection name and dataset name.
					DatasetContent dc = m.getDatasetContent();
					dc.setCollection( d.getCollection() );
					dc.setDatasetName( d.getDatasetAttributes().getName() );
					m.setDatasetContent( dc );
					
					
				}
				*/
			} else {
				System.out.println( "populateMetadata: list of Metadata was empty.");
			}
		}
		
	}
	
	
	private void insertComposers( List<Metadata> meta ) {
		logger.info( "Calling insertComposers()...");
		logger.debug( "meta size is " + meta.size() );
		
		List<Metadata> uniqueByComposer = meta.stream().filter( ClefUtility.distinctByKey( Metadata::getComposer ) ).collect( Collectors.toList() );
		
		if ( ! uniqueByComposer.isEmpty() ) {
			
			ComposerDAO cdao = new ComposerDAO();
			
			List<Composer> uniqueComposers = cdao.mapFromMetadata( uniqueByComposer );
			
			int inserted = cdao.batchInsert( uniqueComposers );
			logger.debug( "insertComposers(): num inserted = " + inserted);
			List<Composer> allComposers = cdao.selectAll();
			logger.debug( "allComposers size is: " + allComposers.size() );
			if ( ! allComposers.isEmpty() ) {
				cdao.updateMetadata( meta, allComposers );
			}
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
		logger.info( "Calling insertEras()...");
		// Get Metadata instances with unique values for Era
		List<Metadata> uniqueByEra = meta.stream().filter( ClefUtility.distinctByKey( Metadata::getEra ) ).collect( Collectors.toList() );
		
		if ( ! uniqueByEra.isEmpty() ) {
			logger.debug( "uniqueByEra size is " + uniqueByEra.size() );
			EraDAO ed = new EraDAO();
			// Extract the Era instances from the list of Metadata instances with unique values for Era
			List<Era> uniqueEras = ed.mapFromMetadata( uniqueByEra );
			
			// Insert the unique era values into the database. Return the count of rows inserted.
			int inserted = ed.batchInsert( uniqueEras );
			if ( inserted > 0 ) {
				// If any eras were inserted, select all eras
				List<Era> allEras = ed.selectAll();
				
				// Update the entire list of metadata, matching on era value.
				ed.updateMetadata( meta, allEras );
			} else {
				logger.info( "Count of inserted eras = " + inserted );
			}
		} else {
			System.out.println( "insertEras: no Metadata with unique eras found.");
		}
	}
	
	private void insertTags( List<Metadata> meta ) {
		logger.info( "Calling insertTags()...");
		
		TagDAO tdao = new TagDAO();
		List<Tag> tags = tdao.mapFromMetadata( meta );
		
		int inserted = tdao.batchInsert( tags );
		logger.debug( "Count of inserted tags = " + inserted );
		
		List<Tag> allTags = tdao.selectAll();
		tdao.updateMetadata( meta, allTags );
	}
	
	
	private void insertTagRelations( List<Metadata> meta ) {
		logger.info( "Calling insertTagRelations()...");
	}
	
	
	/**
	 * 
	 * @param meta
	 */
	private void insertWorkTypes( List<Metadata> meta ) {
		
		logger.info( "Calling insertWorkTypes()..." ); 
		logger.debug( "meta size is " + meta.size() );
		
		List<Metadata> uniqueByWorkType = meta.stream().filter( ClefUtility.distinctByKey( Metadata::getWorkType ) ).collect( Collectors.toList() );
		
		if ( ! uniqueByWorkType.isEmpty() ) {
			WorkTypeDAO wtd = new WorkTypeDAO();
			logger.debug( "uniqueByWorkType size is " + uniqueByWorkType.size() );
			List<WorkType> uniqueWorkTypes = wtd.mapFromMetadata( uniqueByWorkType );
			
			int inserted = wtd.batchInsert( uniqueWorkTypes );
			logger.debug( "insertWorkTypes(): num inserted = " + inserted );
			if ( inserted > 0 ) {
				List<WorkType> allWorkTypes = wtd.selectAll();
				
				wtd.updateMetadata( meta, allWorkTypes );
			}
		} else {
			logger.info( "insertWorkTypes(): unique work types was empty");
		}
	}
	
	
	/**
	 * 
	 * @param meta
	 */
	private void insertWorks( List<Metadata> meta ) {
		logger.debug( "Calling insertWorks()...");
		WorkDAO wd = new WorkDAO();
		List<Work> works = wd.mapFromMetadata( meta );
		
		int inserted = wd.batchInsert( works );
		logger.debug( "insertWorks(): num inserted = " + inserted );
		List<Work> allWorks = wd.selectAll();
		if ( ! allWorks.isEmpty() ) {
			wd.updateMetadata( meta, allWorks );
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


	@Override
	public void run(String... args) throws Exception {
		
		logger.info( "Invoking MetadataLoader...");

		List<Dataset> dsets = new ArrayList<Dataset>();
		try {
			logger.info( "Trying to discover datasets...");
			dsets = Dataset.discoverDatasets();
			
			this.populateMetadata( dsets );
			
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
		
	}
}
