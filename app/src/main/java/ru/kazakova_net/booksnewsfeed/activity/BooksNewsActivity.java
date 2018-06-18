package ru.kazakova_net.booksnewsfeed.activity;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.kazakova_net.booksnewsfeed.loader.BooksNewsLoader;
import ru.kazakova_net.booksnewsfeed.model.BooksNews;
import ru.kazakova_net.booksnewsfeed.adapter.BooksNewsAdapter;

import ru.kazakova_net.booksnewsfeed.R;

public class BooksNewsActivity extends AppCompatActivity implements LoaderCallbacks<List<BooksNews>> {
    public static final String LOG_TAG = BooksNewsActivity.class.getName();

    /**
     * URL for books news from The Guardian
     */
    private static final String THEGUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?q=books&tag=books/books&api-key=test&format=json&page-size=30&show-fields=trailText,thumbnail&order-by=newest";

    /**
     * Constant value for the books news loader ID
     */
    private static final int BOOKS_NEWS_LOADER_ID = 1;

    /**
     * Adapter for the list_books_news of books news
     */
    private BooksNewsAdapter mAdapter;

    /**
     * TextView that is displayed when the list_books_news is empty
     */
    private TextView mEmptyStateTextView;
    private View mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_news);

        // Find a reference to the {@link ListView} in the layout
        ListView booksNewsListView = findViewById(R.id.list_books_news);

        // Create a new adapter that takes an empty list of books news as input
        mAdapter = new BooksNewsAdapter(BooksNewsActivity.this, new ArrayList<BooksNews>());

        mEmptyStateTextView = findViewById(R.id.empty_view);
        booksNewsListView.setEmptyView(mEmptyStateTextView);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        booksNewsListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected books news
        booksNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current books news that was clicked on
                BooksNews currentBooksNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri booksNewsUri = Uri.parse(currentBooksNews.getUrl());

                // Create a new intent to view the books news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, booksNewsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        mLoadingIndicator = findViewById(R.id.loading_indicator);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

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
            mLoadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(getString(R.string.no_internet_connection));
        }
    }

    @Override
    public Loader<List<BooksNews>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "TEST: onCreateLoader()");

        // Create a new loader for the given URL
        return new BooksNewsLoader(this, THEGUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<BooksNews>> loader, List<BooksNews> data) {
        Log.d(LOG_TAG, "TEST: onLoadFinished()");

        // Hide loading indicator because the data has been loaded
        mLoadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No books news found."
        mEmptyStateTextView.setText(getString(R.string.empty_state));

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Books}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BooksNews>> loader) {
        Log.d(LOG_TAG, "TEST: onLoaderReset()");

        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
