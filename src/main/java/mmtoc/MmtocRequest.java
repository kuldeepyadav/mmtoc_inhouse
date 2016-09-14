package mmtoc;

public class MmtocRequest {

    private final String video_id;
    private final int type;

    public MmtocRequest(String id, int type) {
        this.video_id = id;
        this.type = type;
    }

    public String getVideoId() {
        return video_id;
    }

    public int getType() {
        return type;
    }
}