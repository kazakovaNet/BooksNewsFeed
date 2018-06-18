package ru.kazakova_net.booksnewsfeed.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.kazakova_net.booksnewsfeed.R;
import ru.kazakova_net.booksnewsfeed.model.BooksNews;

/**
 * Created by Kazakova_net on 18.06.2018.
 */
public class BooksNewsAdapter extends ArrayAdapter<BooksNews> {
    private final static String LOG_TAG = BooksNewsAdapter.class.getSimpleName();
    
    public BooksNewsAdapter(@NonNull Activity context, @NonNull List<BooksNews> booksNews) {
        super(context, 0, booksNews);
    }
    
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.books_news_list_item, parent, false);
        }
        
        ImageView thumbnailImageView = listItemView.findViewById(R.id.thumbnail);
        TextView webPublicationDateTextView = listItemView.findViewById(R.id.web_publication_date);
        TextView webTitleTextView = listItemView.findViewById(R.id.web_title);
        TextView trailTextTextView = listItemView.findViewById(R.id.trail_text);
        
        BooksNews booksNews = getItem(position);
        
        if (booksNews != null) {
            Picasso.get().load(booksNews.getThumbnail()).into(thumbnailImageView);
            
            webTitleTextView.setText(booksNews.getWebTitle());
            trailTextTextView.setText(booksNews.getTrailText());
            webPublicationDateTextView.setText(formatDate(booksNews.getWebPublicationDate()));
        }
        
        return listItemView;
    }
    
    /**
     * Return the formatted date string (i.e. "03.05.2018, 22:15") from a Date object.
     */
    private String formatDate(String stringDateObject) {
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
}
