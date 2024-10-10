public class Erper {
    String url;
    String reason;
    public Erper(String url, String reportReason) {
        this.url = url;
        this.reason = reportReason;
    }

    public Erper(String url) {
        this.url = url;

    }

    public String getURL(){
        return this.url;

    }
    public void setURL(String url){
        this.url = url;
    }

    public String getReason(){
        return this.reason;


    }
    public void setReason(String reason){
        this.reason = reason;

    }

}
