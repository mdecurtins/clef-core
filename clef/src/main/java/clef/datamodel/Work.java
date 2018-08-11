package clef.common.datamodel;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Work {

	private String catalog;
	private String catalog_number;
	private String composition_date;
	private String era;
	private String title;
	private String type;
	
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
	
	public String getEra() {
		return era;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getType() {
		return type;
	}
	
	public void setCatalog( String c ) {
		this.catalog = c;
	}
	
	public void setCatalogNumber( String cn ) {
		this.catalog_number = cn;
	}
	
	public void setCompositionDate( String cd ) {
		this.composition_date = cd;
	}
	
	public void setEra( String e ) {
		this.era = e;
	}
	
	public void setTitle( String t ) {
		this.title = t;
	}
	
	public void setType( String ty ) {
		this.type = ty;
	}
	
}
