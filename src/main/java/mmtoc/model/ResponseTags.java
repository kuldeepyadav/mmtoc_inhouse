package mmtoc.model;

import org.json.JSONObject;

public class ResponseTags
{
	private final boolean success;
	private final String message;
	private final String tags;
	
	public ResponseTags(boolean success,String message, String tags)
	{
		this.success=success;
		this.message=message;
		this.tags=tags;
	}

	

	public boolean isSuccess() {
		return success;
	}



	public String getTags() {
		return tags;
	}



	public String getMessage() {
		return message;
	}
	
	
}