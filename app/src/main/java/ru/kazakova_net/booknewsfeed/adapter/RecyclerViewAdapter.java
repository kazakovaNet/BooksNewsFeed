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
    private Context mContext;
    private List<BookNews> mBookNews;
    private BookNews mCurrentBookNews;

    public RecyclerViewAdapter(Context context, List<BookNews> bookNews) {
        mContext = context;
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
        mCurrentBookNews = mBookNews.get(position);
        
        // Filling the view with data
        if (mCurrentBookNews != null) {
            Picasso.get()
                    // Start an image request using the specified path
                    .load(mCurrentBookNews.getThumbnail())
                    // Set download placeholder
                    .placeholder(R.drawable.placeholder_image)
                    // Asynchronously fulfills the request into the specified {@link ImageView}
                    .into(holder.mThumbnailImageView);
            
            holder.mWebTitleTextView.setText(mCurrentBookNews.getWebTitle());
            holder.mTrailTextTextView.setText(formatTrailText(mCurrentBookNews.getTrailText()));
            holder.mWebPublicationDateTextView.setText(formatDate(mCurrentBookNews.getWebPublicationDate()));
            holder.mSectionNameTextView.setText(mCurrentBookNews.getSectionName());
            holder.mContributorTextView.setText(mCurrentBookNews.getContributor());
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

            // Set an item click listener on the ListView, which sends an intent to a web browser
            // to open a website with more information about the selected book news
            layoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri booksNewsUri = null;
                    if (mCurrentBookNews != null) {
                        booksNewsUri = Uri.parse(mCurrentBookNews.getUrl());
                    }

                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, booksNewsUri);
                    mContext.startActivity(websiteIntent);
                }
            });
            
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
