package ru.kazakova_net.booknewsfeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.kazakova_net.booknewsfeed.R;
import ru.kazakova_net.booknewsfeed.model.BookNews;

/**
 * Responsible for linking Java code elements with View-components
 */
public class BookNewsAdapter extends RecyclerView.Adapter<BookNewsAdapter.BookNewsViewHolder> {
    public static final String AD = "ad";
    private static final String LOG_TAG = BookNewsAdapter.class.getSimpleName();
    
    // List of objects
    private List<BookNews> mObjectList = new ArrayList<>();
    
    private Context mContext;
    
    public BookNewsAdapter(Context context) {
        mContext = context;
    }
    
    /**
     * Called to create a BookNewsViewHolder object, the constructor of which
     * the created View-component is transmitted, with which in the future
     * bind Java objects
     */
    @NonNull
    @Override
    public BookNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_news_list_item, null);
        
        return new BookNewsViewHolder(view);
    }
    
    /**
     * Responsible for Java object and View
     */
    @Override
    public void onBindViewHolder(@NonNull final BookNewsViewHolder holder, int position) {
        // Find the current book news that was clicked on
        final BookNews currentBookNews = mObjectList.get(position);
        
        // Binding Data to the Appearance
        holder.bind(currentBookNews);
        
        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected book news
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri booksNewsUri = null;
                if (currentBookNews != null) {
                    booksNewsUri = Uri.parse(currentBookNews.getUrl());
                }
                
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, booksNewsUri);
                mContext.startActivity(websiteIntent);
            }
        });
    }
    
    /**
     * Tells the number of items in the list
     */
    @Override
    public int getItemCount() {
        return mObjectList.size();
    }
    
    /**
     * Fills the adapter collection
     *
     * @param objectCollection data to display
     */
    public void setItems(Collection<BookNews> objectCollection) {
        mObjectList.addAll(objectCollection);
        
        // Tell the adapter to know that the list of items has changed
        // and he needs to redraw the items on the screen
        notifyDataSetChanged();
    }
    
    /**
     * Clears the adapter collection
     */
    public void clearItems() {
        mObjectList.clear();
        
        // Tell the adapter to know that the list of items has changed
        // and he needs to redraw the items on the screen
        notifyDataSetChanged();
    }
    
    /**
     * Provides a direct link to each View-component
     * Used to cache View-components and then quickly access them
     */
    class BookNewsViewHolder extends RecyclerView.ViewHolder {
        private final String LOG_TAG = BookNewsViewHolder.class.getSimpleName();
        
        // View-list line components
        private ImageView thumbnailImageView;
        private TextView webPublicationDateTextView;
        private TextView webTitleTextView;
        private TextView trailTextTextView;
        private TextView sectionNameTextView;
        private TextView contributorTextView;
        
        /**
         * Creates a new {@link BookNewsViewHolder} object and looks for all child components
         *
         * @param itemView View-component line
         */
        private BookNewsViewHolder(final View itemView) {
            super(itemView);
            
            thumbnailImageView = itemView.findViewById(R.id.thumbnail);
            webPublicationDateTextView = itemView.findViewById(R.id.web_publication_date);
            webTitleTextView = itemView.findViewById(R.id.web_title);
            trailTextTextView = itemView.findViewById(R.id.trail_text);
            sectionNameTextView = itemView.findViewById(R.id.section_name);
            contributorTextView = itemView.findViewById(R.id.contributor);
        }
        
        /**
         * Fills with data View-components
         *
         * @param bookNews current data object
         */
        private void bind(BookNews bookNews) {
            // Filling the view with data
            if (bookNews != null) {
                if (bookNews.getThumbnail().equals("")) {
                    thumbnailImageView.setImageDrawable(
                            mContext.getResources().getDrawable(R.drawable.placeholder_image));
                } else {
                    Picasso.get()
                            // Start an image request using the specified path
                            .load(bookNews.getThumbnail())
                            // Set download placeholder
                            .placeholder(R.drawable.placeholder_image)
                            // Asynchronously fulfills the request into the specified {@link ImageView}
                            .into(thumbnailImageView);
                }
                
                webTitleTextView.setText(bookNews.getWebTitle());
                trailTextTextView.setText(formatTrailText(bookNews.getTrailText()));
                webPublicationDateTextView.setText(formatDate(bookNews.getWebPublicationDate()));
                sectionNameTextView.setText(bookNews.getSectionName());
                contributorTextView.setText(bookNews.getContributor());
            }
        }
        
        /**
         * Return the formatted date string (i.e. "03.05.2018, 22:15") from a string data
         */
        private String formatDate(String stringDateObject) {
            if (stringDateObject.equals("")) {
                return stringDateObject;
            }
            
            SimpleDateFormat dateFormatInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault());
            
            Date date = new Date();
            
            try {
                date = dateFormatInput.parse(stringDateObject);
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Problem parsing the books news webPublicationDate", e);
            }
            
            return dateFormatOutput.format(date);
        }
        
        /**
         * Returns a formatted string cleared from html tags
         */
        private String formatTrailText(String trailText) {
            return Html.fromHtml(trailText).toString();
        }
    }
}
