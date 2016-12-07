package mmtoc.async;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
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
	private File mRootDir;
	private File mOutputDir;
	private File mMmtocDir;
	private File mSlidesDir;
	
	 
   
	
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
    	   
    	updateStatus(1); // Started process
    	if(!createDirectories()){
    		updateStatus(-1);
			
    		
		}
		PrintWriter requestLog = new PrintWriter(new FileWriter(mConfig.getRequests_logpath()+mRequest.getVideo_id()+".log"));
    	updateStatus(2); //Created directories
		downloadVideo(requestLog);
		requestLog.flush();
		updateStatus(3);// Downloaded video and srt
		List<String>commands = new ArrayList<String>();
		
        commands.add(mConfig.getCommand1_part1());
        commands.add(mConfig.getRequests_folder()+mRequest.getVideo_id());
        executeCommand(commands,requestLog,COMMAND_TYPE_MMTOC);
        
        updateStatus(4);
        requestLog.flush();
        commands = new ArrayList<String>();
        commands.add(mConfig.getCommand2_part1());
        commands.add(mConfig.getRequests_folder()+mRequest.getVideo_id()+"_mmtoc");
        commands.add(mConfig.getCommand2_part2());
        executeCommand(commands,requestLog,COMMAND_TYPE_MMTOC);
        
        requestLog.flush();
        updateStatus(5);
        
        commands = new ArrayList<String>();
        commands.add(mConfig.getCommand3_part1());
        commands.add(mConfig.getRequests_folder()+mRequest.getVideo_id()+"_mmtoc");
        commands.add(mConfig.getCommand3_part2());
        commands.add(mConfig.getCommand3_part3());
        commands.add(mConfig.getCommand3_part4());
        executeCommand(commands,requestLog,COMMAND_TYPE_MMTOC);
        
        requestLog.flush();
        updateStatus(6);
        
        requestLog.close();
        
        fillOutputDirectory();
        
        //OutputJson.createOutputJson(true,mConfig,mRequest.getVideo_id(),null);
        
        updateStatus(7);
		

	} catch (IOException | InterruptedException e) {
		
		e.printStackTrace();
	}
    	
    	
        
	  	 
        System.out.println("Done Job: " );
        
    }
    
    
    
    
    private void downloadVideo(PrintWriter requestLog) throws IOException, InterruptedException {
    	if(mRequest.getType().equalsIgnoreCase("youtube")){
    		List<String> commands=new ArrayList<>();
    		commands.add(mConfig.getYoutube_command());
    		if(mConfig.getUse_proxy()){
    			commands.add("--proxy");
    			commands.add("\""+ mConfig.getProxy_string()+":"+mConfig.getProxy_port()+"\"");
    		}
    		commands.add(mConfig.getYoutube_command2());
    		commands.add(mConfig.getYoutube_command3());
    		commands.add(mConfig.getYoutube_command4());
    		commands.add("--sub-lang");
    		commands.add("en");
    		commands.add("--convert-subs");
    		commands.add("srt");
    		commands.add(mConfig.getYoutube_command7());
    		commands.add(mRequest.getVideo_id());
    		
    	
    		StringBuilder sb = new StringBuilder();
    		for (String s : commands)
    		{
    		    sb.append(s);
    		    sb.append("\t");
    		}

    		System.out.println(sb.toString());
    		executeCommand(commands,requestLog,COMMAND_TYPE_YOUTUBEDL);
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
    	File videoTarget=new File(mRootDir+"/"+mRequest.getVideo_id()+"."+ext);
    	
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
    
    
public void executeCommand(List<String> commands,PrintWriter requestLog, int commandType) throws IOException,InterruptedException{
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(getWorkingDirectory(commandType));
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
            			requestLog.println(line);
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

File getWorkingDirectory(int commandType){
	
	switch(commandType){
	case COMMAND_TYPE_YOUTUBEDL:
		return mRootDir;
	case COMMAND_TYPE_MMTOC:
		return new File(mConfig.getMmtoc_directory());
	}
	return mRootDir;
}


String getErrorMessage(int commandType){
	
	switch(commandType){
	case COMMAND_TYPE_YOUTUBEDL:
		return "Error in downloading Youtube Video";
	}
	return "Error";
}

public boolean createDirectories(){
	
	//logger.info(LogInfoHeader.getHeader(ttid)+"Creating directories for ttid: " + ttid);
			File logsDir=new File(mConfig.getRequests_logpath());
			if(!logsDir.exists()){
				logsDir.mkdirs();
			}
//			mOutputDir =new File(mConfig.getOutput_directory());
//			if(!mOutputDir.exists()){
//				mOutputDir.mkdirs();
//			}
//			
		 mRootDir = new File(mConfig.getRequests_folder()+mRequest.getVideo_id());
		 mMmtocDir = new File(mConfig.getRequests_folder()+mRequest.getVideo_id() + "_mmtoc");
		 mOutputDir = new File(mConfig.getRequests_folder()+mRequest.getVideo_id() + "_output");
		
		//logger.info(LogInfoHeader.getHeader(ttid) + "Deleting Existing mmtoc directory");
		
		deleteDirectory(mMmtocDir);
		deleteDirectory(mOutputDir);
		boolean success= mOutputDir.mkdirs();
		if (!success) {
			//logger.error(LogInfoHeader.getHeader(ttid)+ExceptionMessages.getExceptionObject().getString("6") + ttid);
			return false;
		}
			
		if(!mRootDir.exists() || !mRootDir.isDirectory()){
			// boolean success = (new File(config.getTtidFolder()+ttid)).mkdirs();
			 success = mRootDir.mkdirs();
			if (!success) {
				//logger.error(LogInfoHeader.getHeader(ttid)+ExceptionMessages.getExceptionObject().getString("6") + ttid);
				return false;
			}
			else{
				//logger.info(LogInfoHeader.getHeader(ttid)+"Successfully created ttid directory for: " + ttid);
			}
				
		}
		
		//dir = new File(config.getTtidFolder()+ttid +"/" +"Slides_"+ ttid);
		 mSlidesDir = new File(mConfig.getRequests_folder()+mRequest.getVideo_id()+"/" +"Slides_"+ mRequest.getVideo_id());
		
		if(!mSlidesDir.exists() || !mSlidesDir.isDirectory()){
			 success = mSlidesDir.mkdirs();
			if (!success) {
				//logger.error(LogInfoHeader.getHeader(ttid)+ExceptionMessages.getExceptionObject().getString("7") + ttid);
				return false;
			}
			else{
				//logger.info(LogInfoHeader.getHeader(ttid)+"Successfully created slides directory for ttid: " + ttid);
			}
		}
		return true;
		
}


public void deleteDirectory(File mmtocDir){
	
	
	if(mmtocDir.exists()){
	
		if(mmtocDir.isDirectory()){
		
			String[]entries = mmtocDir.list();
			for(String s: entries){
				File currentFile = new File(mmtocDir.getPath(),s);
				deleteDirectory(currentFile);
			}
		
			mmtocDir.delete();
		}
		else{
			mmtocDir.delete();
		}
	}
	
}

void fillOutputDirectory() throws IOException
{
	String commonPathSource = mConfig.getRequests_folder() + mRequest.getVideo_id() + "_mmtoc/"
			+ mRequest.getVideo_id() + "_tocs/";
	
	Files.copy(new File(commonPathSource + mRequest.getVideo_id() + "_text_.txt").toPath(),new File(mOutputDir.getPath()+"/"+"tableKeywords.txt").toPath() , StandardCopyOption.REPLACE_EXISTING);
	
	Files.copy(new File(commonPathSource + mRequest.getVideo_id() + "_time_.txt").toPath(),new File(mOutputDir.getPath()+"/"+"time.txt").toPath() , StandardCopyOption.REPLACE_EXISTING);
	
	String tagsPath = mConfig.getRequests_folder() + mRequest.getVideo_id() + "_mmtoc/" + mRequest.getVideo_id()
			+ "_frames_top_word_saliencies.txt";
	
	Files.copy(new File(tagsPath).toPath(),new File(mOutputDir.getPath()+"/"+"top_visualword_saliencies.txt").toPath(),StandardCopyOption.REPLACE_EXISTING );
	
	List<Integer> imageList = OutputJson.getImageNames(mConfig, mRequest.getVideo_id());
	
	
	File imagesDirectory=new File(mOutputDir+"/"+"images");
	if(!imagesDirectory.exists())
		imagesDirectory.mkdirs();
	
	
	if(imageList!=null)
	{
		BufferedReader timeBuffer = new BufferedReader(new FileReader(
				new File(commonPathSource + mRequest.getVideo_id() + "_time_.txt")));
		
		for (Integer ii : imageList) {
			Files.copy(new File(commonPathSource + mRequest.getVideo_id() + "_slides/frame_"+ii+".png").toPath(),new File(mOutputDir.getPath()+"/images/"+timeBuffer.readLine().trim()
					+".png").toPath() , StandardCopyOption.REPLACE_EXISTING);
			
		
		}
		
		timeBuffer.close();
		
	}
}



	
}
