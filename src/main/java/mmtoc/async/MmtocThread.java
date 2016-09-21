package mmtoc.async;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.Random;

import org.springframework.context.ApplicationContext;

import mmtoc.config.Config;
import mmtoc.context.AppContext;
import mmtoc.dao.IMmtocRequestDao;
import mmtoc.model.MmtocRequest;


public class MmtocThread implements Runnable {
	 
    private static int nth = 0;

    private final int id = ++nth;
    private final int speed;
    private MmtocRequest request;
   
	
//    @Value("${mmtoc.youtube_directory}")
//	private String youtubeDirectory;
    
	

    public MmtocThread(MmtocRequest request) {
        // draw 0-99 and give +1 to progress at least by 1%:
        this.speed = new Random().nextInt(99) + 1;
        this.request=request;
    }
    @Override
    public void run() {
        System.out.println("Starting printer work: " + id );
        for (int i = 0; i <= 100; i += speed) {
            try {
                MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                // ignore
            }
            System.out.printf("worker %d, done %d%%%n", id, i);
        }
       
         request.setStatus(1);
   	 	 ApplicationContext ctx=AppContext.getApplicationContext();
	  	 IMmtocRequestDao dao = ctx.getBean(IMmtocRequestDao.class);
	  	
	  	 dao.saveRequest(request);
	  	 
        System.out.println("Done PrinterJob: " + id + ctx.getBean(Config.class).getYoutube_directory());
        
    }
}
