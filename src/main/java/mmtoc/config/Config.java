package mmtoc.config;

import org.springframework.beans.factory.annotation.Value;

public class Config
{
	@Value("${mmtoc.youtube_directory}")
	private String youtube_directory;
	
	@Value("${mmtoc.youtube_command}")
	private String youtube_command;
	
	@Value("${mmtoc.youtube_command2}")
	private String youtube_command2;

	public String getYoutube_directory() {
		return youtube_directory;
	}

	public String getYoutube_command() {
		return youtube_command;
	} 
	
	
	public String getYoutube_command2() {
		return youtube_command2;
	} 
	
	
	
	
	
}