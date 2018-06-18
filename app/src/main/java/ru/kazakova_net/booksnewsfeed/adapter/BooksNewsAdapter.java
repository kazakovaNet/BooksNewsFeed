package ru.kazakova_net.booksnewsfeed.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
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
        
        TextView webPublicationDateTextView = listItemView.findViewById(R.id.web_publication_date);
        TextView webTitleTextView = listItemView.findViewById(R.id.web_title);
        TextView trailTextTextView = listItemView.findViewById(R.id.trail_text);
        
        BooksNews booksNews = getItem(position);
        if (booksNews != null) {
            
            webTitleTextView.setText(booksNews.getWebTitle());
            trailTextTextView.setText(booksNews.getTrailText());
            
            String webPublicationDate = booksNews.getWebPublicationDate();
            webPublicationDateTextView.setText(webPublicationDate);
        }
        
        return listItemView;
    }
    
    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy", Locale.getDefault());
        return dateFormat.format(dateObject);
    }
    
    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return timeFormat.format(dateObject);
    }
    
    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(magnitude);
    }
}
