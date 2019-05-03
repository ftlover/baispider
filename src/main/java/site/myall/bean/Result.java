package site.myall.bean;

public class Result {

    private String title;

    private String baisiUrl;

    private String fileString;

    public Result(String title, String baisiUrl, String fileString) {
        this.title = title;
        this.baisiUrl = baisiUrl;
        this.fileString = fileString;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBaisiUrl(String baisiUrl) {
        this.baisiUrl = baisiUrl;
    }

    public void setFileString(String fileString) {
        this.fileString = fileString;
    }

    public String getTitle() {
        return title;
    }

    public String getBaisiUrl() {
        return baisiUrl;
    }

    public String getFileString() {
        return fileString;
    }

    @Override
    public String toString() {
        return "Result{" +
                "title='" + title + '\'' +
                ", baisiUrl='" + baisiUrl + '\'' +
                ", fileString='" + fileString + '\'' +
                '}';
    }
}
