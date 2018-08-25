package clef.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Work extends ClefDataObject {

	@JsonIgnore
	private int id;
	private String catalog;
	private String catalog_number;
	@JsonIgnore
	private int composerId;
	private String composition_date;
	
	@JsonIgnore
	private int datasetContentsId;
	
	private String era;
	@JsonIgnore
	private int eraId;
	
	private String pcn;
	private String title;
	private String type;
	
	@JsonIgnore
	private int workTypeId;
	
	public Work() {
		
	}
	
	public Work( String title ) {
		this.title = title;
	}
	
	public String getCatalog() {
		return catalog;
	}
	
	public String getCatalogNumber() {
		return catalog_number;
	}
	
	public String getCompositionDate() {
		return composition_date;
	}
	
	@JsonIgnore
	public int getComposerId() {
		return composerId;
	}
	
	@JsonIgnore
	public int getDatasetContentsId() {
		return datasetContentsId;
	}
	
	public String getEra() {
		return era;
	}
	
	@JsonIgnore
	public int getEraId() {
		return eraId;
	}
	
	@JsonIgnore
	public int getId() {
		return id;
	}
	
	public String getPCN() {
		return pcn;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getType() {
		return type;
	}
	
	@JsonIgnore
	public int getWorkTypeId() {
		return workTypeId;
	}
	
	public void setCatalog( String c ) {
		this.catalog = c;
	}

	
	public void setCatalogNumber( String cn ) {
		this.catalog_number = cn;
	}

	@JsonProperty
	public void setComposerId( int id ) {
		this.composerId = id;
	}
	
	public void setCompositionDate( String cd ) {
		this.composition_date = cd;
	}
	
	@JsonProperty
	public void setDatasetContentsId( int id ) {
		this.datasetContentsId = id;
	}
	
	public void setEra( String e ) {
		this.era = e;
	}
	
	@JsonProperty
	public void setEraId( int id ) {
		this.eraId = id;
	}
	
	@JsonProperty
	public void setId( int id ) {
		this.id = id;
	}
	
	public void setPCN( String pcn ) {
		this.pcn = pcn;
	}
	
	public void setTitle( String t ) {
		this.title = t;
	}
	
	public void setType( String ty ) {
		this.type = ty;
	}
	
	@JsonProperty
	public void setWorkTypeId( int id ) {
		this.workTypeId = id;
	}
	
}
