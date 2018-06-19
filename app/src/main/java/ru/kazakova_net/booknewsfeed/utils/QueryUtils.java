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
package ru.kazakova_net.booknewsfeed.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import ru.kazakova_net.booknewsfeed.model.BookNews;

/**
 * Helper methods related to requesting and receiving book news from The Guardian.
 */
public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods
     */
    private QueryUtils() {
    }
    
    /**
     * Query the The Guardian dataset and return a list of {@link BookNews} objects.
     */
    public static List<BookNews> fetchBookNewsData(String requestUrl) {
        Log.d(LOG_TAG, "TEST: fetchBookNewsData()");
        
        // Create URL object
        URL url = createUrl(requestUrl);
        
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request. ", e);
        }
        
        // Extract relevant fields from the JSON response and create a list of BookNews
        // Return the list of book news
        return extractFeatureFromJson(jsonResponse);
    }
    
    /**
     * Returns new {@link URL} object from the given string URL.
     */
    private static URL createUrl(String requestUrl) {
        URL url = null;
        
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        
        return url;
    }
    
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        
        return jsonResponse;
    }
    
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            
            String line = reader.readLine();
            
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        
        return output.toString();
    }
    
    /**
     * Return a list of {@link BookNews} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<BookNews> extractFeatureFromJson(String bookNewsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookNewsJSON)) {
            return null;
        }
        
        // Create an empty ArrayList that we can start adding book news to
        List<BookNews> bookNewsList = new ArrayList<>();
        
        // Try to parse the bookNewsJSON. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject responseObject = new JSONObject(bookNewsJSON).getJSONObject("response");
            
            // Extract the JSONArray associated with the key called "results",
            // which represents a list of book news
            JSONArray bookNewsArray = responseObject.getJSONArray("results");
            
            // For each book news in the bookNewsArray, create an BookNews object
            for (int i = 0; i < bookNewsArray.length(); i++) {
                // Get a single book news at position i within the list of book news
                JSONObject currentBookNews = bookNewsArray.getJSONObject(i);
                
                // Parsing JSON data
                String webPublicationDate = currentBookNews.getString("webPublicationDate");
                String webTitle = currentBookNews.getString("webTitle");
                String webUrl = currentBookNews.getString("webUrl");
                JSONObject fields = currentBookNews.getJSONObject("fields");
                String trailText = fields.getString("trailText");
                String thumbnail = fields.getString("thumbnail");
                String sectionName = currentBookNews.getString("sectionName");
                
                // Create a new BookNews object with the data from the JSON response
                BookNews bookNews = new BookNews(webPublicationDate, webTitle, webUrl, trailText, thumbnail, sectionName);
                
                // Add the new {@link Earthquake} to the list of bookNews.
                bookNewsList.add(bookNews);
            }
            
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book news JSON results", e);
        }
        
        // Return the list of book news
        return bookNewsList;
    }
}
