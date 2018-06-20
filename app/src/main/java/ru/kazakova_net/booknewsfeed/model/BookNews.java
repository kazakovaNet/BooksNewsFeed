/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.kazakova_net.booknewsfeed.model;

/**
 * Created by Kazakova_net on 18.06.2018.
 */
public class BookNews {
    private String webPublicationDate;
    private String webTitle;
    private String webUrl;
    private String trailText;
    private String thumbnail;
    private String sectionName;
    private String contributor;
    
    /**
     * Constructor of new {@link BookNews} object
     *
     * @param webPublicationDate The combined date and time of publication
     * @param webTitle           Title of the article
     * @param webUrl             URL for the article
     * @param trailText          Short article text
     * @param thumbnail          Thumbnail of the article
     * @param sectionName        Name of the section that article belongs
     * @param contributor        Name of author of article
     */
    public BookNews(String webPublicationDate, String webTitle, String webUrl, String trailText, String thumbnail, String sectionName, String contributor) {
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.trailText = trailText;
        this.thumbnail = thumbnail;
        this.sectionName = sectionName;
        this.contributor = contributor;
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
    
    public String getSectionName() {
        return sectionName;
    }
    
    public String getContributor() {
        return contributor;
    }
}
