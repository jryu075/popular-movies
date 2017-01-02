package com.junghoryu.popularmoviesudacity;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jryu075 on 2016-12-20.
 */

class GetMovieJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetMovieJsonData";

    private List<Photo> mPhotoList = null;

    private String mGetMovieListBaseURL;
    private String mApiKey;

    private String mGetMovieBaseUrl;
    private String mSize;
    private final OnDataAvailable mCallback;
    private boolean runningOnSameThread = false;

    private int DASH_AT_BEGINNING = 1;




    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    public GetMovieJsonData(OnDataAvailable callback, String getMovieBaseUrl, String apiKey, String getMovieListBaseURL, String size) {
        mGetMovieBaseUrl = getMovieBaseUrl;
        mApiKey = apiKey;
        mGetMovieListBaseURL = getMovieListBaseURL;
        mSize = size;
        mCallback = callback;
    }

    void executeOnSameThread(String sortingMethodTag) {
        Log.d(TAG, "executeOnSameThread starts");
        runningOnSameThread = true;
        String destinationUri = createMovieListUri(sortingMethodTag);


        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread ends");
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground starts");
        String destinationUri = createMovieListUri(params[0]);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground ends");

        return mPhotoList;
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute starts");
        if(mCallback != null) {
            mCallback.onDataAvailable(mPhotoList, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute ends");
    }

    private String createMovieListUri(String sortingMethodTag) {
        return Uri.parse(mGetMovieListBaseURL).buildUpon()
                .appendPath(sortingMethodTag)
                .appendQueryParameter("api_key", mApiKey)
                .build().toString();
    }


    private String createPhotoUri(String movieBaseUrl,String size, String posterPath) {
        return Uri.parse(movieBaseUrl).buildUpon()
                .appendPath(size)
                .appendPath(posterPath)
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete starts. Status = " + status);

        if(status == DownloadStatus.OK) {
            mPhotoList = new ArrayList<>();

            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("results");

                for(int i=0; i<itemsArray.length(); i++){
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);

                    //Dash char at the beginning of the poster_path url was causing unnecessary addition
                    //of "%2F" when appended during Uri construction.
                    String posterPath = jsonPhoto.getString("poster_path").substring(DASH_AT_BEGINNING);
                    String photoUri = createPhotoUri(mGetMovieBaseUrl, mSize, posterPath);

                    String summary = jsonPhoto.getString("overview");
                    String releaseDate = jsonPhoto.getString("release_date");
                    String title = jsonPhoto.getString("title");
                    float rating = Float.valueOf(jsonPhoto.getString("vote_average"));



                    Photo photoObject = new Photo(summary, photoUri, releaseDate, title, rating);
                    mPhotoList.add(photoObject);

                    Log.d(TAG, "onDownloadComplete " + photoObject.toString());
                }
            } catch(JSONException jsone) {
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data" + jsone.getMessage() );
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }
        
        if(runningOnSameThread && mCallback != null) {
            //now inform the caller that processsing is done - possibly returning null if there was an error;
            mCallback.onDataAvailable(mPhotoList, status);
            
        }
        Log.d(TAG, "onDownloadComplete ends");
    }
}
