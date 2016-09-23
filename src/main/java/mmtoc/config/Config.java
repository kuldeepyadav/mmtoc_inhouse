package mmtoc.config;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Value;

public class Config
{
	@Value("${mmtoc.youtube_directory}")
	private String youtube_directory;
	
	@Value("${mmtoc.youtube_command}")
	private String youtube_command;
	
	@Value("${mmtoc.youtube_command2}")
	private String youtube_command2;
	
	@Value("${mmtoc.others_directory}")
	private String others_directory;
	
	@Value("${web.use_proxy}")
	private Boolean use_proxy;
	
	@Value("${web.proxy_string}")
	private String proxy_string;
	
	@Value("${web.proxy_port}")
	private int proxy_port;

	public String getYoutube_directory() {
		return youtube_directory;
	}
	
	
	public String getOthers_directory() {
		return others_directory;
	}


	public String getYoutube_command() {
		return youtube_command;
	} 
	
	
	public String getYoutube_command2() {
		return youtube_command2;
	}


	public Boolean getUse_proxy() {
		return use_proxy;
	}


	public String getProxy_string() {
		return proxy_string;
	}


	public int getProxy_port() {
		return proxy_port;
	}


	
	
	
	
	
	
}