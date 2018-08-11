package clef.datamodel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import clef.common.ClefException;
import clef.datamodel.Work;

public class InsertWork extends ClefStatement implements Insert {

	private static final String SQL = "INSERT INTO %s VALUES ( ?, ?, ?, ?, ?, ? );";
	
	public InsertWork( Work w ) {
		super( w );
	}


	public PreparedStatement prepareAndBind( Connection conn ) throws SQLException, ClefException {
	
		String tbl = this.getTablename();
		String sql = String.format( SQL, tbl );
		
		PreparedStatement ps = conn.prepareStatement( sql, PreparedStatement.RETURN_GENERATED_KEYS );
		
		Work w = (Work) this.cdo;
		
		// Set the work title.
		if ( w.getTitle() != null && ! w.getTitle().equals("") ) {
			ps.setString( 1, w.getTitle() );
		} else {
			// This will throw a SQLException when executed, as title is declared NOT NULL.
			ps.setNull( 1, Types.VARCHAR );
		}
		
		// Set the composer catalog name, if one exists.
		if ( w.getCatalog() != null && ! w.getCatalog().equals("") ) {
			ps.setString( 2, w.getCatalog() );
		} else {
			ps.setNull( 2, Types.VARCHAR );
		}
		
		// Set the catalog number, if one exists.
		if ( w.getCatalogNumber() != null && ! w.getCatalogNumber().equals("") ) {
			ps.setString( 3, w.getCatalogNumber() );
		} else {
			ps.setNull( 3, Types.VARCHAR );
		}
		
		// Set the composer foreign key reference.
		if ( w.getComposerId() != 0 ) {
			ps.setInt( 4, w.getComposerId() );
		} else {
			// This will throw a SQLException when executed, as composer_id is declared NOT NULL.
			ps.setNull( 4, Types.SMALLINT );
		}
		
		// Set the era, if one exists.
		if ( w.getEraId() != 0 ) {
			ps.setInt( 5, w.getEraId() );
		} else {
			ps.setNull( 5, Types.SMALLINT );
		}
		
		// Set the work type (genre), if one exists.
		if ( w.getWorkTypeId() != 0 ) {
			ps.setInt( 6, w.getWorkTypeId() );
		} else {
			ps.setNull( 6, Types.SMALLINT );
		}
		
		return ps;
	}
	
	
}
