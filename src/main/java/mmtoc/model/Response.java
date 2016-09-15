package mmtoc.model;

public class Response
{
	private final int request_id;
	private final String message;
	
	public Response(int request_id,String message)
	{
		this.message=message;
		this.request_id=request_id;
	}

	public int getRequest_id() {
		return request_id;
	}

	public String getMessage() {
		return message;
	}
	
	
}