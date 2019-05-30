package com.example.android.newsapp;

public class News {

    private String title;

    private String newsSection;

    private String publishedDate;

    private String storyUrl;

    public News (String mTitle, String mNewsSection, String mPublishedDate, String mStoryUrl){

        title = mTitle;

        newsSection = mNewsSection;

        publishedDate = mPublishedDate;

        storyUrl = mStoryUrl;
    }

    public String getTitle(){return title;}

    public String getNewsSection(){return newsSection;}

    public String getPublishedDateAndTime(){return publishedDate;}

    public String getStoryUrl(){return storyUrl;}

}
