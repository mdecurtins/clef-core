package clef.datamodel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import clef.common.ClefException;
import clef.datamodel.DatasetContent;

public class InsertDatasetContents extends ClefStatement implements Insert {

	private static final String SQL = "INSERT INTO %s VALUES ( ?, ?, ?, ? );";
	
	public InsertDatasetContents( DatasetContent dc ) {
		super( dc );
	}

	
	public PreparedStatement prepareAndBind( Connection conn ) throws SQLException, ClefException {
		
		String tbl = this.getTablename();
		String sql = String.format( SQL, tbl );
		
		PreparedStatement ps = conn.prepareStatement( sql, PreparedStatement.RETURN_GENERATED_KEYS );
		
		DatasetContent dc = (DatasetContent) this.cdo;
		
		if ( dc.getCollection() != null ) {
			ps.setString( 1,  dc.getCollection() );
		} else {
			// This will throw a SQLException when executed, as collection is declared NOT NULL.
			ps.setNull( 1, Types.VARCHAR );
		}
		
		if ( dc.getDatasetName() != null ) {
			ps.setString( 2, dc.getDatasetName() );
		} else {
			// This will throw a SQLException when executed, as dataset_name is declared NOT NULL.
			ps.setNull( 2, Types.VARCHAR );
		}
		
		if ( dc.getFilename() != null ) {
			ps.setString( 3, dc.getFilename() );
		} else {
			// This will throw a SQLException when executed, as filename is declared NOT NULL.
			ps.setNull( 3, Types.VARCHAR );
		}
	
		
		return ps;
	}
	
	
}
