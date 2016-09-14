package mmtoc;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MmtocController {

    @RequestMapping("/request")
    public MmtocRequest greeting(@RequestParam(value="video", defaultValue="") String video_id, @RequestParam(value="type", defaultValue="-1") int type) {
        return new MmtocRequest(video_id,type);
    }
}