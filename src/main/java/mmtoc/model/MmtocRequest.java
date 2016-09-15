package mmtoc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_request")
public class MmtocRequest {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="video_id")
    private  String video_id;
	
	@Column(name="type")
    private  String type;
	
	@Column(name="url")
    private  String url;
	
	@Column(name="status")
	private int status;

    public MmtocRequest() {
        
    }
    
    

    public String getVideo_id() {
		return video_id;
	}



	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}
	
	
	






	
}