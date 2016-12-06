package mmtoc.async;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import mmtoc.config.Config;

public class OutputJson {

//	private static final Logger logger = LogManager.getLogger(OutputJson.class			.getName());

	public static List<Integer> getImageNames(Config config, String ttid) {
		List<Integer> list = new ArrayList<Integer>();

		String folderPath = config.getRequests_folder() + ttid + "_mmtoc/" + ttid
				+ "_tocs/" + ttid + "_slides";

		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File Name: " + listOfFiles[i].getName());
//				logger.info(LogInfoHeader.getHeader(ttid)
//						+ listOfFiles[i].getName());
				String pngPart = listOfFiles[i].getName().split("_")[1];
				list.add(Integer.parseInt(pngPart.split("\\.")[0]));
			}
		}

		Collections.sort(list);
		return list;

	}

	public static String toPrettyFormat(String jsonString) {
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(jsonString).getAsJsonObject();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);

		return prettyJson;
	}

	public static boolean createOutputJson(boolean success, Config config,  
			String ttid, Exception ex) throws IOException {

		try {

			PrintWriter out = new PrintWriter(new FileWriter(new File(
					config.getRequests_folder() + ttid + "_output.json")));

			JSONObject obj = new JSONObject();
			JSONObject extras=new JSONObject();
//			if(j!=null && j.has("extras") && j.getJSONObject("extras")!=null){
//				extras=j.getJSONObject("extras");
//			}

			obj.put("videoId", ttid);

			if (success) {
				obj.put("result", "success");
			} else {
				// Abhishek: to send message as a separate field in return message
				obj.put("result", "fail");
				obj.put("message", ex.getMessage());
				obj.put("extras", extras);
				out.println(toPrettyFormat(obj.toString()));
				out.flush();
				out.close();
				return false;
			}

//			if (j.has("professor"))
//				obj.put("professor", j.getString("professor"));
//
//			if (j.has("subject"))
//				obj.put("subject", j.getString("subject"));
//
//			if (j.has("topic"))
//				obj.put("topic", j.getString("topic"));
//
//			if (j.has("institute"))
//				obj.put("institute", j.getString("institute"));
//
//			if (j.has("department"))
//				obj.put("department", j.getString("department"));

			JSONArray list = new JSONArray();

			List<Integer> imageList = getImageNames(config, ttid);

			String commonPath = config.getRequests_folder() + ttid + "_mmtoc/"
					+ ttid + "_tocs/";
			BufferedReader textBuffer = new BufferedReader(new FileReader(
					new File(commonPath + ttid + "_text_.txt")));
			BufferedReader timeBuffer = new BufferedReader(new FileReader(
					new File(commonPath + ttid + "_time_.txt")));

			for (Integer ii : imageList) {
				JSONObject jj = new JSONObject();
				jj.put("topic", textBuffer.readLine().trim());
				jj.put("time", timeBuffer.readLine().trim());
				jj.put("frameurl", "frame_" + ii + ".png");
				list.put(jj);
			}
			textBuffer.close();
			timeBuffer.close();

			if(list!=null && list.length()>0){
				obj.put("topics", list);
			}
			else
			{
				obj = new JSONObject();
				
				obj.put("ttid", ttid);
				
				obj.put("result", "fail");
				obj.put("message"," Unable to process MMTOC. No topics found");
				obj.put("extras", extras);
				out.println(toPrettyFormat(obj.toString()));
				out.flush();
				out.close();
				return false;
			}
			
			String tagsPath = config.getRequests_folder() + ttid + "_mmtoc/" + ttid
					+ "_frames_top_word_saliencies.txt";
			BufferedReader tagReader = new BufferedReader(new FileReader(
					new File(tagsPath)));
			String line = null;

			JSONArray tagArrayObj = new JSONArray();
			
			int linesRead = 0;
			while ((line = tagReader.readLine()) != null) {
				if (!line.contains(":"))
					continue;
			
				linesRead++; 
				JSONObject tagObj = new JSONObject();
				String[] tagAndTimes = line.split(":");
				String tag = tagAndTimes[0].trim();
				List<String> times = new ArrayList<String>();
				 
				if(tag.equals(""))
					continue;
					
				
				if((tagAndTimes.length<2)||(tagAndTimes[1].trim().equals(""))){
					continue;
				}
				else{					
					for (String t : tagAndTimes[1].trim().split(" ")){						
						times.add(t);
					}					
				}
				
				tagObj.put("tag", tagAndTimes[0].trim());
				tagObj.put("times", times);
				tagArrayObj.put(tagObj);
				
			}
			tagReader.close();
			
			if(tagArrayObj!=null && tagArrayObj.length()>0){
				obj.put("tags", tagArrayObj);
			}
			else{
				
				obj = new JSONObject();
				
				obj.put("ttid", ttid);
				
				obj.put("result", "fail");
				obj.put("message"," Unable to process MMTOC. No tags found");
				obj.put("extras", extras);
				out.println(toPrettyFormat(obj.toString()));
				out.flush();
				out.close();
				return false;
				
			}
			
			
//			if((linesRead==0) && imageList.isEmpty()){
//				JSONObject objFail = new JSONObject();
//
//				objFail.put("result", ExceptionMessages.getExceptionObject().getString("39") + ttid);
//				out.println(toPrettyFormat(objFail.toString()));
//			}
//			else{
//				out.println(toPrettyFormat(obj.toString()));
//			}
			// Add an empty message field in success case
			obj.put("message","");
			obj.put("extras", extras);
			out.println(toPrettyFormat(obj.toString()));
			
			out.flush();
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
//			logger.error(LogInfoHeader.getHeader(ttid)
//					+ ExceptionMessages.getExceptionObject().getString("17")
//					+ ttid);
			throw e;
		}

	}

	public static void createOutputJson(Config config,String ttid) throws IOException {

		try {
			PrintWriter out = new PrintWriter(new FileWriter(new File(
					config.getRequests_folder()+ ttid + "_output.json")));

			JSONObject obj = new JSONObject();
			// Abhishek: to send a separate message in case of failure
			obj.put("ttid", "000000");
			obj.put("result", "fail");
			obj.put("message"," Mandatory field ttid absent");
			out.println(toPrettyFormat(obj.toString()));
			out.flush();
			out.close();			
		}

		catch (IOException e) {
//			logger.error(LogInfoHeader.getHeader()
//					+ ExceptionMessages.getExceptionObject().getString("17"));
			e.printStackTrace();
			throw e;
		}

	}

}
