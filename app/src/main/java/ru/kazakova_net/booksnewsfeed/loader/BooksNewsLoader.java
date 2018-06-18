package ru.kazakova_net.booksnewsfeed.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import ru.kazakova_net.booksnewsfeed.utils.QueryUtils;
import ru.kazakova_net.booksnewsfeed.model.BooksNews;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class BooksNewsLoader extends AsyncTaskLoader<List<BooksNews>> {
    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = BooksNewsLoader.class.getName();
    
    /**
     * Query URL
     */
    private String mUrl;
    
    public BooksNewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    
    /**
     * This is on a background thread.
     */
    @Override
    public List<BooksNews> loadInBackground() {
        Log.d(LOG_TAG, "TEST: loadInBackground()");
        
        if (mUrl == null) {
            return null;
        }
        
        // Perform the network request, parse the response, and extract a list of earthquakes.
        return QueryUtils.fetchBooksNewsData(mUrl);
    }
    
    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG, "TEST: onStartLoading()");
        
        forceLoad();
    }
    
}
