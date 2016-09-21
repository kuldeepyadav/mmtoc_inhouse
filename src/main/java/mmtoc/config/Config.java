package mmtoc.config;

import org.springframework.beans.factory.annotation.Value;

public class Config
{
	@Value("${mmtoc.youtube_directory}")
	private String youtube_directory;

	public String getYoutube_directory() {
		return youtube_directory;
	} 
	
	
	
}