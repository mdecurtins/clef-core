package clef.datamodel.metadata;

import clef.datamodel.Composer;
import clef.datamodel.Work;
import clef.datamodel.WorkType;

public interface MetadataService {

	public Composer getComposer();
	public Work getWork();
	public WorkType getWorkType();
}
