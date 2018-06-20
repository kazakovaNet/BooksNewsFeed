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
package ru.kazakova_net.booknewsfeed.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
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

import ru.kazakova_net.booknewsfeed.R;
import ru.kazakova_net.booknewsfeed.model.BookNews;

/**
 * Created by Kazakova_net on 18.06.2018.
 */
public class BookNewsAdapter extends ArrayAdapter<BookNews> {
    private final static String LOG_TAG = BookNewsAdapter.class.getSimpleName();
    
    public BookNewsAdapter(@NonNull Activity context, @NonNull List<BookNews> bookNews) {
        super(context, 0, bookNews);
    }
    
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_news_list_item, parent, false);
        }
        
        ImageView thumbnailImageView = convertView.findViewById(R.id.thumbnail);
        TextView webPublicationDateTextView = convertView.findViewById(R.id.web_publication_date);
        TextView webTitleTextView = convertView.findViewById(R.id.web_title);
        TextView trailTextTextView = convertView.findViewById(R.id.trail_text);
        TextView sectionNameTextView = convertView.findViewById(R.id.section_name);
        TextView contributorTextView = convertView.findViewById(R.id.contributor);
        
        BookNews currentBookNews = getItem(position);
        
        // Filling the view with data
        if (currentBookNews != null) {
            Picasso.get()
                    // Start an image request using the specified path
                    .load(currentBookNews.getThumbnail())
                    // Set download placeholder
                    .placeholder(R.drawable.placeholder_image)
                    // Asynchronously fulfills the request into the specified {@link ImageView}
                    .into(thumbnailImageView);
            
            webTitleTextView.setText(currentBookNews.getWebTitle());
            trailTextTextView.setText(formatTrailText(currentBookNews.getTrailText()));
            webPublicationDateTextView.setText(formatDate(currentBookNews.getWebPublicationDate()));
            sectionNameTextView.setText(currentBookNews.getSectionName());
            contributorTextView.setText(currentBookNews.getContributor());
        }
        
        return convertView;
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
