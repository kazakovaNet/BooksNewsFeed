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
package ru.kazakova_net.booknewsfeed.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import ru.kazakova_net.booknewsfeed.model.BookNews;
import ru.kazakova_net.booknewsfeed.utils.QueryUtils;

/**
 * Loads a list of book news by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class BookNewsLoader extends AsyncTaskLoader<List<BookNews>> {
    private static final String LOG_TAG = BookNewsLoader.class.getName();
    
    /**
     * Query URL
     */
    private String url;
    
    public BookNewsLoader(Context context, String url) {
        super(context);
        
        this.url = url;
    }
    
    /**
     * This is on a background thread
     */
    @Override
    public List<BookNews> loadInBackground() {
        Log.d(LOG_TAG, "TEST: loadInBackground()");
        
        if (url == null) {
            return null;
        }
        
        // Perform the network request, parse the response, and extract a list of book news
        return QueryUtils.fetchBookNewsData(url);
    }
    
    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG, "TEST: onStartLoading()");
        
        forceLoad();
    }
}
