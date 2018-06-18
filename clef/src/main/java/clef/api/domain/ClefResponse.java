package clef.api.domain;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClefResponse {

	private double apiVersion;
	private Object executionTime = null;
	private ClefResponseBody responseBody = null;
	private String responseType;
	private String serviceName;
	private long timestamp;
	
	public ClefResponse( double apiVersion, String responseType, String serviceName ) {
		this.apiVersion = apiVersion;
		this.responseType = responseType;
		this.serviceName = serviceName;
		
		// Set timestamp of this response.
		Instant now = Instant.now();
		this.timestamp = now.getEpochSecond();
		
	}

	public double getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(double apiVersion) {
		this.apiVersion = apiVersion;
	}

	public Object getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Object executionTime) {
		this.executionTime = executionTime;
	}

	public ClefResponseBody getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(ClefResponseBody responseBody) {
		this.responseBody = responseBody;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
