package clef.datamodel.db;

import clef.datamodel.metadata.Metadata;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class ClefResultDAO {

	public List<Metadata> getJoinedMetadata( String dset_names, String filenames ) {
		Database db = new Database();
		try {
			Connection conn = db.getConnection();
			CallableStatement cs = conn.prepareCall( "{CALL getJoinedMetadata( ?, ? )}" );
			cs.setString( 1, dset_names );
			cs.setString( 2, filenames );
			
			ResultSet rs = cs.executeQuery();
			while ( rs.next() ) {
				
			}
		} catch ( SQLException sqle ) {
			sqle.printStackTrace();
		}
		return null;
	}
}
