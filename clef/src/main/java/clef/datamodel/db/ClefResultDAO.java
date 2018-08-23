package clef.datamodel.db;

import clef.datamodel.Composer;
import clef.datamodel.DatasetContent;
import clef.datamodel.Work;
import clef.datamodel.metadata.Metadata;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClefResultDAO {
	
	private static final Logger logger = LoggerFactory.getLogger( ClefResultDAO.class );

	public List<Metadata> getJoinedMetadata( String dset_names, String filenames ) {
		List<Metadata> resultData = new ArrayList<Metadata>();
		Database db = new Database();
		try {
			logger.debug( "Preparing to call getJoinedMetadata( " + dset_names + ", " + filenames + " )..." );
			Connection conn = db.getConnection();
			CallableStatement cs = conn.prepareCall( "{CALL getJoinedMetadata( ?, ? )}" );
			cs.setString( 1, dset_names );
			cs.setString( 2, filenames );
			
			ResultSet rs = cs.executeQuery();
			while ( rs.next() ) {
				Metadata m = new Metadata();
				Composer c = new Composer();
				DatasetContent dc = new DatasetContent();
				Work w = new Work();
				
				dc.setDatasetName( rs.getString( "dataset_name" ) );
				dc.setFilename( rs.getString( "filename" ) );
				
				w.setTitle( rs.getString( "title" ) );
				w.setCatalog( rs.getString( "catalog" ) );
				w.setCatalogNumber( rs.getString( "catalog_number" ) );
				w.setType( rs.getString( "work_type" ) );
				w.setEra( rs.getString( "era" ) );
				
				c.setName( rs.getString( "composer_name" ) );
				c.setDates( rs.getInt( "born" ), rs.getInt( "died" ) );
				
				m.setComposer( c );
				m.setDatasetContent( dc );
				m.setWork( w );
				
				resultData.add( m );
			}
			rs.close();
		} catch ( SQLException sqle ) {
			logger.error( sqle.getMessage() );
			sqle.printStackTrace();
		}
		return resultData;
	}
}
