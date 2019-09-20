package com.example.android.newsapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;

import java.util.ArrayList;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {

    private String url;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public NewsLoader(Context context, String mUrl){
        super(context);
        url = mUrl;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public ArrayList<News> loadInBackground(){
        if(url == null){
            return null;
        }

        ArrayList<News> news = QueryUtils.fetchNewsDada(url);
        return news;
    }
}
