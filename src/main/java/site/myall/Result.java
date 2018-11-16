package site.myall;

public class Result {
    private String title;
    private String baisi_url;
    private String cat_url;
    private String image_urls;

    public Result(String title, String baisi_url, String cat_url, String image_urls) {
        this.title = title;
        this.baisi_url = baisi_url;
        this.cat_url = cat_url;
        this.image_urls = image_urls;
    }

    public String getTitle() {
        return title;
    }

    public String getBaisi_url() {
        return baisi_url;
    }

    public String getCat_url() {
        return cat_url;
    }

    public String getImage_urls() {
        return image_urls;
    }
}
