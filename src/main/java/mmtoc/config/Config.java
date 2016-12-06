package mmtoc.config;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Value;

public class Config
{
//	@Value("${mmtoc.youtube_directory}")
//	private String youtube_directory;
	
	@Value("${mmtoc.youtube_command}")
	private String youtube_command;
	
	@Value("${mmtoc.youtube_command2}")
	private String youtube_command2;
	
	@Value("${mmtoc.youtube_command3}")
	private String youtube_command3;
	
	@Value("${mmtoc.youtube_command4}")
	private String youtube_command4;
	
	@Value("${mmtoc.youtube_command5}")
	private String youtube_command5;
	
	@Value("${mmtoc.youtube_command6}")
	private String youtube_command6;
	
	@Value("${mmtoc.youtube_command7}")
	private String youtube_command7;
	
//	@Value("${mmtoc.others_directory}")
//	private String others_directory;
	
	@Value("${mmtoc.mmtoc_directory}")
	private String mmtoc_directory;
	
	@Value("${mmtoc.output_directory}")
	private String output_directory;
	
	
	@Value("${mmtoc.command1_part1}")
	private String command1_part1;
	
	@Value("${mmtoc.command2_part1}")
	private String command2_part1;
	
	@Value("${mmtoc.command2_part2}")
	private String command2_part2;
	
	@Value("${mmtoc.command3_part1}")
	private String command3_part1;
	
	@Value("${mmtoc.command3_part2}")
	private String command3_part2;
	
	@Value("${mmtoc.command3_part3}")
	private String command3_part3;
	
	@Value("${mmtoc.command3_part4}")
	private String command3_part4;
	
	@Value("${mmtoc.requests_folder}")
	private String requests_folder;
	
	@Value("${mmtoc.requests_logpath}")
	private String requests_logpath;
	
	@Value("${web.use_proxy}")
	private Boolean use_proxy;
	
	@Value("${web.proxy_string}")
	private String proxy_string;
	
	@Value("${web.proxy_port}")
	private int proxy_port;
	
	

//	public String getYoutube_directory() {
//		return youtube_directory;
//	}
//	
//	
//	public String getOthers_directory() {
//		return others_directory;
//	}


	public String getYoutube_command() {
		return youtube_command;
	} 
	
	
	public String getYoutube_command2() {
		return youtube_command2;
	}
	
	

	public String getYoutube_command3() {
		return youtube_command3;
	}
	
	
	public String getYoutube_command4() {
		return youtube_command4;
	}
	
	
	public String getYoutube_command5() {
		return youtube_command5;
	}
	
	

	public String getYoutube_command6() {
		return youtube_command6;
	}
	
	
	public String getYoutube_command7() {
		return youtube_command7;
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


	public String getMmtoc_directory() {
		return mmtoc_directory;
	}

	public String getOutput_directory() {
		return output_directory;
	}


	public String getRequests_folder() {
		return requests_folder;
	}
	
	public String getRequests_logpath() {
		return requests_logpath;
	}
	
	public String getCommand1_part1() {
		return command1_part1;
	}


	public String getCommand2_part1() {
		return command2_part1;
	}


	public String getCommand2_part2() {
		return command2_part2;
	}


	public String getCommand3_part1() {
		return command3_part1;
	}


	public String getCommand3_part2() {
		return command3_part2;
	}


	public String getCommand3_part3() {
		return command3_part3;
	}


	public String getCommand3_part4() {
		return command3_part4;
	}
	
	
	
	
	
	
	
	


	
	
	
	
	
	
}