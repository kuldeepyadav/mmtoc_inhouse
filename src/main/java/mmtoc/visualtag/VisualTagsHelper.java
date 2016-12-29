package mmtoc.visualtag;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;

import mmtoc.config.Config;
import mmtoc.context.AppContext;
import mmtoc.model.ResponseTags;

public class VisualTagsHelper {
	
	public static ResponseTags getVideoTags(String videoId)
	{
		 ApplicationContext mCtx = AppContext.getApplicationContext();
		 Config config=mCtx.getBean(Config.class);
		 
		
		File visualTagsFile=new File(config.getRequests_folder()+videoId+"_output/top_visualword_saliencies.txt");
	
		if(visualTagsFile.exists()){
			 try {
				 JSONObject obj=new JSONObject();
				 List<String> lines = FileUtils.readLines(visualTagsFile, "UTF-8");
				 
			   
					for (String line:lines) {
					   
						String[] splitLine=line.split(":");
						String word=splitLine[0];
						String[] positions=splitLine[1].split(" ");
						JSONArray mJSONArray = new JSONArray(Arrays.asList(positions));
						obj.put(word, mJSONArray);
						
						
						
					}
					 
					
					
					return new ResponseTags(true,"",obj.toString());
				
			
			 } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new ResponseTags(false,e.getMessage(),null);
				}
			
			
			
		}
		else
		{
			return new ResponseTags(false,"Can't find the file",null);
		}
		
		
		
	}

}
