package clef.datamodel.db;

import java.util.List;

import clef.datamodel.metadata.Metadata;

public class TagRelationDAO extends ClefDAO {

	public int batchInsert( List<Metadata> meta ) {
		String sql = "INSERT INTO tag_relations ( work_id, tag_id ) VALUES ( ?, ? );";
		return 0;
	}
}
