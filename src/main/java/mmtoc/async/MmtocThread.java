package mmtoc.async;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import mmtoc.config.Config;
import mmtoc.context.AppContext;
import mmtoc.dao.IMmtocRequestDao;
import mmtoc.model.MmtocRequest;
import mmtoc.utils.Utils;


public class MmtocThread implements Runnable {
	
	public static final int COMMAND_TYPE_YOUTUBEDL=0;
	public static final int COMMAND_TYPE_VIDEODL=1;
	public static final int COMMAND_TYPE_MMTOC=2;
	 
  
    private MmtocRequest mRequest;

	//private ApplicationContext mCtx;

	private IMmtocRequestDao mDao;
	
	private String youtubeVideosDirectory;
	
	private Config mConfig;
	
	 
   
	
//    @Value("${mmtoc.youtube_directory}")
//	private String youtubeDirectory;
    
	

    public MmtocThread(MmtocRequest request) {
        this.mRequest=request;
        ApplicationContext mCtx = AppContext.getApplicationContext();
	  	mDao = mCtx.getBean(IMmtocRequestDao.class);
	  	mConfig=mCtx.getBean(Config.class);
	  	
    }
    
    
    
    
    @Override
    public void run() {
    	
       
       try {
    	   
    	updateStatus(1);
		downloadVideo();
		updateStatus(2);
	} catch (IOException | InterruptedException e) {
		
		e.printStackTrace();
	}
    	
    	
        
	  	 
        System.out.println("Done Job: " );
        
    }
    
    
    
    
    private void downloadVideo() throws IOException, InterruptedException {
    	if(mRequest.getType().equalsIgnoreCase("youtube")){
    		List<String> commands=new ArrayList<>();
    		commands.add(mConfig.getYoutube_command());
    		commands.add(mConfig.getYoutube_command2());
    		commands.add(mRequest.getVideo_id());
    		if(mConfig.getUse_proxy()){
    			commands.add("--proxy "+ mConfig.getProxy_string()+":"+mConfig.getProxy_port());
    		}
    		
    		executeCommand(commands,null,COMMAND_TYPE_YOUTUBEDL);
    	}
    	else{
    		downloadOtherVideo();	
    	}
		
		
	}
    
    void downloadOtherVideo() throws IOException
    {
    	String videoUrlString=mRequest.getUrl();
    	URL videoUrl;
	
			videoUrl = new URL(videoUrlString);
		
    	String ext = videoUrlString.substring(videoUrlString.lastIndexOf('.') + 1);
    	File videoTarget=new File(mConfig.getOthers_directory()+"/"+mRequest.getVideo_id()+"."+ext);
    	
    	System.out.println(mConfig.getProxy_string()+":"+mConfig.getProxy_port());
    	if(mConfig.getUse_proxy()){
    		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					mConfig.getProxy_string(), mConfig.getProxy_port()));
    		Utils.downloadUsingProxy(proxy, videoUrl, videoTarget);
    	}
    	else{
    	org.apache.commons.io.FileUtils.copyURLToFile(videoUrl,videoTarget );
    	}
    	
		
    }




	void updateStatus(int status){
    	mRequest.setStatus(status);
    	mRequest=mDao.saveRequest(mRequest);
    }
    
    
public void executeCommand(List<String> commands,PrintWriter ttidLog, int commandType) throws IOException,InterruptedException{
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(new File(getWorkingDirectory(commandType)));
        pb.redirectErrorStream(true);
        try {

        	//logger.info(LogInfoHeader.getHeader(ttid)+pb.command().toString());
            Process prs = pb.start();
            
            BufferedReader reader =
            		new BufferedReader(new InputStreamReader(prs.getInputStream()));
            		String line = null;
            		while ((line=reader.readLine()) != null)
            		{
            			System.out.println(line);
            			//ttidLog.println(line);
            			//logger.info(LogInfoHeader.getHeader(ttid)+line);
            		}
            
            if(prs.waitFor() !=0){
            	//logger.error(LogInfoHeader.getHeader(ttid)+"Process did not complete Successfully!!");
            	prs.destroy();
            	StringBuilder sb = new StringBuilder();
        		
        		for(String str : commands){
        			sb.append(str);
        			sb.append(" ");
        		}
        		//logger.error(LogInfoHeader.getHeader(ttid)+ExceptionMessages.getExceptionObject().getString("18") + ttid + " " + sb.toString());
            	//throw new InterruptedException();
        		// Abhishek: To add message to exception
        		throw new InterruptedException(getErrorMessage(commandType));
            }
            else{
            	//logger.info(LogInfoHeader.getHeader(ttid)+"Process completed Successfully!!");
            }
            
            prs.destroy();

        } catch (IOException e) {
        	StringBuilder sb = new StringBuilder();
    		
    		for(String str : commands){
    			sb.append(str);
    			sb.append(" ");
    		}
    		//logger.error(LogInfoHeader.getHeader(ttid)+ExceptionMessages.getExceptionObject().getString("10") + ttid + " " + sb.toString());
        	throw e;
        }
        catch(InterruptedException e){
        	StringBuilder sb = new StringBuilder();
    		
    		for(String str : commands){
    			sb.append(str);
    			sb.append(" ");
    		}
    		//logger.error(LogInfoHeader.getHeader(ttid)+ExceptionMessages.getExceptionObject().getString("18") + ttid + " " + sb.toString());
        	
        	throw e;
        }
	}

String getWorkingDirectory(int commandType){
	
	switch(commandType){
	case COMMAND_TYPE_YOUTUBEDL:
		return mConfig.getYoutube_directory();
	}
	return mConfig.getYoutube_directory();
}


String getErrorMessage(int commandType){
	
	switch(commandType){
	case COMMAND_TYPE_YOUTUBEDL:
		return "Error in downloading Youtube Video";
	}
	return "Error";
}
	
}
