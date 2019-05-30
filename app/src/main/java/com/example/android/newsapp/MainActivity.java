package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<ArrayList<News>>{

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final String NEWS_URL_BASE = "https://content.guardianapis.com/search?";

    private static final String NEWS_URL_SAMPLE = "https://content.guardianapis.com/search?from-date=2019-05-01&order-by=newest&to-date=2019-05-31&production-office=aus&api-key=test";

    private static final int NEWS_LOADER_ID = 1;

    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    private ImageView mEmptyIcon;

    /** Adapter for the list of earthquakes */
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of news as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_text);

        mEmptyIcon = (ImageView) findViewById(R.id.empty_icon);

        // Set empty View for the situation that no ListView
        View emptyView = (View) findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyView);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if(networkInfo != null && networkInfo.isConnected()){

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID,null, this);

        } else{
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.progress);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        newsListView.setAdapter(mAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){

                News currentNews = mAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getStoryUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int i, Bundle bundle){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String regionOffice = sharedPreferences.getString(
                getString(R.string.key),
                "us"
        );

        Uri baseUri = Uri.parse(NEWS_URL_BASE);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("from-date","2019-05-01");
        uriBuilder.appendQueryParameter("order-by","newest");
        uriBuilder.appendQueryParameter("to-date","2019-05-31");
        uriBuilder.appendQueryParameter("production-office",regionOffice);
        uriBuilder.appendQueryParameter("api-key","test");

        // When loading, empty state icon in invisible
        mEmptyIcon.setVisibility(View.GONE);

        //return new NewsLoader(this, NEWS_URL_SAMPLE);
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> news){

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.progress);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No News Found"
        mEmptyStateTextView.setText(R.string.no_news);

        // Set empty state icon to be visible
        mEmptyIcon.setVisibility(View.VISIBLE);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        if(news != null && !news.isEmpty()){
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader){
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
