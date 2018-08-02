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
package ru.kazakova_net.booknewsfeed.activity;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ru.kazakova_net.booknewsfeed.R;
import ru.kazakova_net.booknewsfeed.adapter.BookNewsAdapter;
import ru.kazakova_net.booknewsfeed.loader.BookNewsLoader;
import ru.kazakova_net.booknewsfeed.model.BookNews;
import ru.kazakova_net.booknewsfeed.view.SimpleDividerItemDecoration;

public class BookNewsActivity extends AppCompatActivity implements LoaderCallbacks<List<BookNews>> {
    public static final String LOG_TAG = BookNewsActivity.class.getName();
    
    /**
     * URL for book news data from The Guardian
     */
    private static final String THE_GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search";
    
    /**
     * Constant value for the book news loader ID
     */
    private static final int BOOKS_NEWS_LOADER_ID = 1;
    
    /**
     * Adapter for the list of book news
     */
    private BookNewsAdapter bookNewsAdapter;
    
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView emptyStateTextView;
    
    /**
     * ProgressBar that is displayed when the list is empty
     */
    private View loadingIndicatorProgressBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_news);
        
        emptyStateTextView = findViewById(R.id.empty_view);
        
        // Set empty state text to display "No book news found."
        emptyStateTextView.setText(getString(R.string.empty_state));
        emptyStateTextView.setVisibility(View.GONE);
        
        loadingIndicatorProgressBar = findViewById(R.id.loading_indicator);
        
        initRecyclerView();
        
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        
        // Get details on the currently active default data network
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOKS_NEWS_LOADER_ID, null, this);
            Log.d(LOG_TAG, "TEST: initLoader()");
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicatorProgressBar.setVisibility(View.GONE);
            
            // Update empty state with no connection error message
            emptyStateTextView.setText(getString(R.string.no_internet_connection));
        }
    }
    
    @Override
    public Loader<List<BookNews>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String pageSize = sharedPrefs.getString(
                getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default));
        
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        
        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(THE_GUARDIAN_REQUEST_URL);
        
        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();
        
        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("q", "books");
        uriBuilder.appendQueryParameter("tag", "books/books");
        uriBuilder.appendQueryParameter("api-key", "664e9d7f-bb72-4e63-a58f-dd684cab8942");
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("page-size", pageSize);
        uriBuilder.appendQueryParameter("show-fields", "trailText,thumbnail");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        
        Log.d(LOG_TAG, "TEST: onCreateLoader()");
        
        // Create a new loader for the completed uri
        // https://content.guardianapis.com/search?q=books&tag=books/books&api-key=664e9d7f-bb72-4e63-a58f-dd684cab8942&format=json&page-size=50&show-fields=trailText,thumbnail&order-by=newest&show-tags=contributor
        return new BookNewsLoader(this, uriBuilder.toString());
    }
    
    @Override
    public void onLoadFinished(Loader<List<BookNews>> loader, List<BookNews> data) {
        Log.d(LOG_TAG, "TEST: onLoadFinished()");
        
        // Hide loading indicator because the data has been loaded
        loadingIndicatorProgressBar.setVisibility(View.GONE);
        
        // Clear the adapter of previous book news data
        bookNewsAdapter.clearItems();
        
        // If there is a valid list of {@link Books}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (!data.isEmpty()) {
            bookNewsAdapter.setItems(data);
        } else {
            // Show empty state message if no data
            emptyStateTextView.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onLoaderReset(Loader<List<BookNews>> loader) {
        Log.d(LOG_TAG, "TEST: onLoaderReset()");
        
        // Loader reset, so we can clear out our existing data.
        bookNewsAdapter.clearItems();
    }
    
    /**
     * Initialize {@link RecyclerView}
     */
    private void initRecyclerView() {
        RecyclerView bookNewsRecyclerView = findViewById(R.id.list_book_news);
        bookNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Adding a separator between elements
        bookNewsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        
        // Create a new adapter that takes an empty list of book news as input
        bookNewsAdapter = new BookNewsAdapter(this);
        bookNewsRecyclerView.setAdapter(bookNewsAdapter);
    }
    
    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    // This method is called whenever an item in the options menu is selected.
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
