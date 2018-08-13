package clef.api.domain.response;

import java.time.Instant;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import clef.api.Application;

/**
 * Superclass of all API response classes.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ClefResponse {

	protected double apiVersion;
	protected String responseType;
	protected String serviceName;
	protected long timestamp;
	
	protected ClefResponse() {
		
		// Set the API version and service name.
		this.apiVersion = Application.VERSION;
		this.serviceName = Application.SERVICE_NAME;
		
		// Set timestamp of this response.
		Instant now = Instant.now();
		this.timestamp = now.getEpochSecond();
	}
	
	/**
	 * 
	 * @param l the data that should be returned to the client in this response
	 * @since 1.0.0
	 */
	public abstract <T> void setData( List<T> l );

	public double getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion( double apiVersion ) {
		this.apiVersion = apiVersion;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType( String responseType ) {
		this.responseType = responseType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName( String serviceName ) {
		this.serviceName = serviceName;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
