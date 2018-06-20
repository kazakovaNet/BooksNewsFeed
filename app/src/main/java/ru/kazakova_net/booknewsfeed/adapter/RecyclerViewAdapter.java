package ru.kazakova_net.booknewsfeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.kazakova_net.booknewsfeed.R;
import ru.kazakova_net.booknewsfeed.model.BookNews;

import static ru.kazakova_net.booknewsfeed.activity.BookNewsActivity.LOG_TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolders> {
    private List<BookNews> mBookNews;
    
    public RecyclerViewAdapter(Context context, List<BookNews> bookNews) {
        mBookNews = bookNews;
    }
    
    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_books_news, null);
        
        return new RecyclerViewHolders(layoutView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolders holder, int position) {
        BookNews currentBookNews = mBookNews.get(position);
        
        // Filling the view with data
        if (currentBookNews != null) {
            Picasso.get()
                    // Start an image request using the specified path
                    .load(currentBookNews.getThumbnail())
                    // Set download placeholder
                    .placeholder(R.drawable.placeholder_image)
                    // Asynchronously fulfills the request into the specified {@link ImageView}
                    .into(holder.mThumbnailImageView);
            
            holder.mWebTitleTextView.setText(currentBookNews.getWebTitle());
            holder.mTrailTextTextView.setText(formatTrailText(currentBookNews.getTrailText()));
            holder.mWebPublicationDateTextView.setText(formatDate(currentBookNews.getWebPublicationDate()));
            holder.mSectionNameTextView.setText(currentBookNews.getSectionName());
            holder.mContributorTextView.setText(currentBookNews.getContributor());
        }
    }
    
    @Override
    public int getItemCount() {
        return mBookNews.size();
    }
    
    class RecyclerViewHolders extends RecyclerView.ViewHolder {
        
        final ImageView mThumbnailImageView;
        final TextView mWebPublicationDateTextView;
        final TextView mWebTitleTextView;
        final TextView mTrailTextTextView;
        final TextView mSectionNameTextView;
        final TextView mContributorTextView;
        
        RecyclerViewHolders(View layoutView) {
            super(layoutView);
            
            mThumbnailImageView = layoutView.findViewById(R.id.thumbnail);
            mWebPublicationDateTextView = layoutView.findViewById(R.id.web_publication_date);
            mWebTitleTextView = layoutView.findViewById(R.id.web_title);
            mTrailTextTextView = layoutView.findViewById(R.id.trail_text);
            mSectionNameTextView = layoutView.findViewById(R.id.section_name);
            mContributorTextView = layoutView.findViewById(R.id.contributor);
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
