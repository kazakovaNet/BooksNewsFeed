package ru.kazakova_net.booksnewsfeed.model;

/**
 * Created by Kazakova_net on 18.06.2018.
 */
public class BooksNews {
    private String webPublicationDate;
    private String webTitle;
    private String webUrl;
    private String trailText;
    private String thumbnail;
    
    /**
     * Constructor of new {@link BooksNews} object
     *
     * @param webPublicationDate The combined date and time of publication
     * @param webTitle           Title of the article
     * @param webUrl             URL for the article
     * @param trailText          Short article text
     * @param thumbnail          Thumbnail of the article
     */
    public BooksNews(String webPublicationDate, String webTitle, String webUrl, String trailText, String thumbnail) {
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.trailText = trailText;
        this.thumbnail = thumbnail;
    }
    
    public String getUrl() {
        return webUrl;
    }
    
    public String getWebPublicationDate() {
        return webPublicationDate;
    }
    
    public String getWebTitle() {
        return webTitle;
    }
    
    public String getTrailText() {
        return trailText;
    }
    
    public String getThumbnail() {
        return thumbnail;
    }
}
