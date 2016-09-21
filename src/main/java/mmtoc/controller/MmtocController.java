package mmtoc.controller;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mmtoc.AppConfig;
import mmtoc.async.MmtocThread;
import mmtoc.dao.IMmtocRequestDao;
import mmtoc.model.MmtocRequest;
import mmtoc.model.Response;

@RestController
public class MmtocController {

//    @RequestMapping("/request")
//    public MmtocRequest request(@RequestParam(value="video", defaultValue="") String video_id, @RequestParam(value="type", defaultValue="-1") int type) {
//        return new MmtocRequest();
//    }
	@Autowired
	IMmtocRequestDao dao;
	final ExecutorService executor = Executors.newSingleThreadExecutor();
	
    
    @RequestMapping(value="/request/",method = RequestMethod.POST)
    public Response createRequest( @RequestBody MmtocRequest mmtocRequest) {
    	try
    	{
//    	 AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
//   	  	 ctx.register(AppConfig.class);
//   	  	 ctx.refresh();
//   	  	 IMmtocRequestDao dao = ctx.getBean(IMmtocRequestDao.class);
   	  	 mmtocRequest=dao.saveRequest(mmtocRequest);
   	  	 executor.execute(new MmtocThread(mmtocRequest));
   	  	
         return new Response(mmtocRequest.getId(),"Success");
    	}
    	catch(Exception ex){
    		ex.printStackTrace();
    		return new Response(-1,"Error:"+ex.getMessage());
    	}
    }
}